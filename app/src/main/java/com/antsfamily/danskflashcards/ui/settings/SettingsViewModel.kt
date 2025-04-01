package com.antsfamily.danskflashcards.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.core.model.mapToErrorType
import com.antsfamily.danskflashcards.core.model.mapToItem
import com.antsfamily.danskflashcards.core.util.toDisplayName
import com.antsfamily.danskflashcards.domain.GetAppVersionUseCase
import com.antsfamily.danskflashcards.domain.GetLoggedInUserUseCase
import com.antsfamily.danskflashcards.domain.GetLearningLanguageUseCase
import com.antsfamily.danskflashcards.domain.GetPrimaryLanguageUseCase
import com.antsfamily.danskflashcards.domain.SetLanguageUseCase
import com.antsfamily.danskflashcards.domain.SignOutWithGoogleUseCase
import com.antsfamily.danskflashcards.domain.model.LanguageType
import com.antsfamily.danskflashcards.ui.onboarding.model.LanguageItem
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val signOutWithGoogleUseCase: SignOutWithGoogleUseCase,
) : ViewModel() {

    private val _showLanguageBottomSheetFlow = MutableSharedFlow<List<LanguageItem>>()
    val showLanguageBottomSheetFlow: SharedFlow<List<LanguageItem>>
        get() = _showLanguageBottomSheetFlow.asSharedFlow()

    private val _navigateToAuthFlow = MutableSharedFlow<Unit>()
    val navigateToAuthFlow: SharedFlow<Unit>
        get() = _navigateToAuthFlow.asSharedFlow()

    private val _state = MutableStateFlow<SettingsUiState>(SettingsUiState.Loading)
    val state: StateFlow<SettingsUiState>
        get() = _state.asStateFlow()

    private var primaryLanguage: LanguageType? = null

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

    fun onLanguageClick() = viewModelScope.launch {
        val learningLanguage = (_state.value as SettingsUiState.Content).learningLanguage
        val languages = LanguageType.entries
            .map { LanguageItem(it, it.toDisplayName() == learningLanguage) }
            .filter { it.languageType != primaryLanguage }
        _showLanguageBottomSheetFlow.emit(languages)
    }

    fun onNewLanguageSelected(item: LanguageItem) = viewModelScope.launch {
        setLanguageUseCase(item.languageType, false)
        val newState =
            (_state.value as SettingsUiState.Content).copy(learningLanguage = item.languageType.toDisplayName())
        _state.value = newState
    }

    private fun getUserData() = viewModelScope.launch {
        val user = getLoggedInUserUseCase()?.mapToItem()
        user?.let {
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
        primaryLanguage = getPrimaryLanguageUseCase()
        val learningLanguage = getLearningLanguageUseCase()
        _state.value = SettingsUiState.Content(
            username = username,
            learningLanguage = learningLanguage.toDisplayName(),
            appVersion = version
        )
    }

    private fun handleErrorState(e: Exception) {
        _state.value = SettingsUiState.Error(e.mapToErrorType())
    }
}