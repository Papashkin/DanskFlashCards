package com.antsfamily.danskflashcards.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.core.model.Avatar
import com.antsfamily.danskflashcards.core.model.ErrorType
import com.antsfamily.danskflashcards.core.model.mapToErrorType
import com.antsfamily.danskflashcards.core.navigation.NAVIGATION_ANIMATION_DURATION
import com.antsfamily.danskflashcards.domain.usecase.GetAppVersionUseCase
import com.antsfamily.danskflashcards.domain.usecase.GetLearningLanguageUseCase
import com.antsfamily.danskflashcards.domain.usecase.GetLoggedInUserUseCase
import com.antsfamily.danskflashcards.domain.usecase.GetPrimaryLanguageUseCase
import com.antsfamily.danskflashcards.domain.usecase.SetLanguageUseCase
import com.antsfamily.danskflashcards.domain.usecase.SetUserAvatarUseCase
import com.antsfamily.danskflashcards.domain.usecase.SetUsernameUseCase
import com.antsfamily.danskflashcards.domain.usecase.SignOutWithGoogleUseCase
import com.antsfamily.danskflashcards.domain.model.LanguageType
import com.antsfamily.danskflashcards.ui.onboarding.model.LanguageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val getAppVersionUseCase: GetAppVersionUseCase,
    private val getLearningLanguageUseCase: GetLearningLanguageUseCase,
    private val getPrimaryLanguageUseCase: GetPrimaryLanguageUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val setUsernameUseCase: SetUsernameUseCase,
    private val signOutWithGoogleUseCase: SignOutWithGoogleUseCase,
    private val setUserAvatarUseCase: SetUserAvatarUseCase,
) : ViewModel() {

    private val _showLearningLanguageBottomSheetFlow = MutableSharedFlow<List<LanguageItem>>()
    val showLearningLanguageBottomSheetFlow: SharedFlow<List<LanguageItem>>
        get() = _showLearningLanguageBottomSheetFlow.asSharedFlow()

    private val _showPrimaryLanguageBottomSheetFlow = MutableSharedFlow<List<LanguageItem>>()
    val showPrimaryLanguageBottomSheetFlow: SharedFlow<List<LanguageItem>>
        get() = _showPrimaryLanguageBottomSheetFlow.asSharedFlow()

    private val _showAvatarChangeDialogFlow = MutableSharedFlow<Unit>()
    val showAvatarChangeDialogFlow: SharedFlow<Unit>
        get() = _showAvatarChangeDialogFlow.asSharedFlow()

    private val _navigateToAuthFlow = MutableSharedFlow<Unit>()
    val navigateToAuthFlow: SharedFlow<Unit>
        get() = _navigateToAuthFlow.asSharedFlow()

    private val _showErrorSnackbarFlow = MutableSharedFlow<ErrorType>()
    val showErrorSnackbarFlow: SharedFlow<ErrorType>
        get() = _showErrorSnackbarFlow.asSharedFlow()

    private val _state = MutableStateFlow<SettingsUiState>(SettingsUiState.Loading)
    val state: StateFlow<SettingsUiState>
        get() = _state.asStateFlow()

    private lateinit var userId: String
    private lateinit var userAvatar: Avatar

    init {
        getUserData()
    }

    fun onRetryClick() {
        _state.value = SettingsUiState.Loading
        getUserData()
    }

    fun onLogOutConfirm() = viewModelScope.launch {
        try {
            _state.value = SettingsUiState.Loading
            signOutWithGoogleUseCase()
        } catch (e: Exception) {
            // no-op
        } finally {
            _navigateToAuthFlow.emit(Unit)
        }
    }

    fun onUsernameChanged(username: String) = viewModelScope.launch {
        try {
            setUsernameUseCase(userId, username.trim())
            val newState = (_state.value as SettingsUiState.Content).copy(
                username = username
            )
            _state.value = newState
        } catch (e: Exception) {
            handleErrorState(e, false)
        }
    }

    fun onAvatarChangeClick() = viewModelScope.launch {
        _showAvatarChangeDialogFlow.emit(Unit)
    }

    fun onAvatarSelected(newAvatar: Avatar) = viewModelScope.launch {
        try {
            setUserAvatarUseCase(userId, newAvatar.ordinal)
            val newState = (_state.value as SettingsUiState.Content).copy(avatar = newAvatar)
            _state.value = newState
        } catch (e: Exception) {
            handleErrorState(e, false)
        }
    }

    fun onLanguageClick(isPrimary: Boolean) = viewModelScope.launch {
        with(_state.value as SettingsUiState.Content) {
            if (isPrimary) {
                showPrimaryLanguageBottomSheet(this)
            } else {
                showLearningLanguageBottomSheet(this)
            }
        }
    }

    private suspend fun showLearningLanguageBottomSheet(content: SettingsUiState.Content) {
        val languages = LanguageType.entries
            .map { LanguageItem(it, it == content.learningLanguage) }
            .filter { it.languageType != content.primaryLanguage }
        _showLearningLanguageBottomSheetFlow.emit(languages)
    }

    private suspend fun showPrimaryLanguageBottomSheet(content: SettingsUiState.Content) {
        val languages = LanguageType.entries
            .map { LanguageItem(it, it == content.primaryLanguage) }
            .filter { it.languageType != content.learningLanguage }
        _showPrimaryLanguageBottomSheetFlow.emit(languages)
    }

    fun onNewLanguageSelected(item: LanguageItem, isPrimary: Boolean) = viewModelScope.launch {
        setLanguageUseCase(item.languageType, isPrimary)
        val content = _state.value as SettingsUiState.Content
        val newContent = content.copy(
            learningLanguage = if (isPrimary) content.learningLanguage else item.languageType,
            primaryLanguage = if (isPrimary) item.languageType else content.primaryLanguage
        )
        _state.value = newContent
    }

    private fun getUserData() = viewModelScope.launch {
        delay(NAVIGATION_ANIMATION_DURATION.toLong())
        val user = getLoggedInUserUseCase()
        user?.let {
            userId = it.id
            userAvatar = it.avatarId?.let { id -> Avatar.entries[id] } ?: Avatar.DEFAULT
            getAppVersion(it.username)
        } ?: run {
            handleErrorState(Exception("user data is empty"))
        }
    }

    private suspend fun getAppVersion(username: String) {
        val version = getAppVersionUseCase()
        version?.let {
            getLanguages(username, it)
        } ?: run {
            handleErrorState(Exception("user data is empty"))
        }
    }

    private suspend fun getLanguages(username: String, version: String) {
        val primaryLanguage = getPrimaryLanguageUseCase()
        val learningLanguage = getLearningLanguageUseCase()
        _state.value = SettingsUiState.Content(
            username = username,
            learningLanguage = learningLanguage,
            primaryLanguage = primaryLanguage,
            appVersion = version,
            avatar = userAvatar
        )
    }

    private suspend fun handleErrorState(e: Exception, isGeneralError: Boolean = true) {
        val errorType = e.mapToErrorType()
        if (isGeneralError) {
            _state.value = SettingsUiState.Error(errorType)
        } else {
            _showErrorSnackbarFlow.emit(errorType)
        }
    }
}