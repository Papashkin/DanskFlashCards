package com.antsfamily.danskflashcards.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.core.model.mapToErrorType
import com.antsfamily.danskflashcards.core.model.mapToItem
import com.antsfamily.danskflashcards.core.util.toDisplayName
import com.antsfamily.danskflashcards.domain.GetAppVersionUseCase
import com.antsfamily.danskflashcards.domain.GetLoggedInUserUseCase
import com.antsfamily.danskflashcards.domain.GetSelectedLanguageUseCase
import com.antsfamily.danskflashcards.domain.SetSelectedLanguageUseCase
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
    private val getSelectedLanguageUseCase: GetSelectedLanguageUseCase,
    private val setSelectedLanguageUseCase: SetSelectedLanguageUseCase,
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
        val selectedLanguage = (_state.value as SettingsUiState.Content).selectedLanguage
        val languages = LanguageType.entries
            .map { LanguageItem(it, it.toDisplayName() == selectedLanguage) }
            .filter { !it.languageType.isEnglish() }
        _showLanguageBottomSheetFlow.emit(languages)
    }

    fun onNewLanguageSelected(item: LanguageItem) = viewModelScope.launch {
        setSelectedLanguageUseCase(item.languageType)
        val newState =
            (_state.value as SettingsUiState.Content).copy(selectedLanguage = item.languageType.toDisplayName())
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
            getSelectedLanguage(username, it)
        } ?: run {
            handleErrorState(Exception("user data is empty"))

        }
    }

    private suspend fun getSelectedLanguage(username: String, version: String) {
        val language = getSelectedLanguageUseCase()
        _state.value = SettingsUiState.Content(username, language.toDisplayName(), version)
    }

    private fun handleErrorState(e: Exception) {
        _state.value = SettingsUiState.Error(e.mapToErrorType())
    }
}