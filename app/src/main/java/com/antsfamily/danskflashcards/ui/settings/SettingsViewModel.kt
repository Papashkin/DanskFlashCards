package com.antsfamily.danskflashcards.ui.settings

import androidx.credentials.ClearCredentialStateRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.core.model.mapToErrorType
import com.antsfamily.danskflashcards.core.model.mapToItem
import com.antsfamily.danskflashcards.domain.GetAppVersionUseCase
import com.antsfamily.danskflashcards.domain.GetLoggedInUserUseCase
import com.antsfamily.danskflashcards.domain.GetSelectedLanguageUseCase
import com.antsfamily.danskflashcards.domain.SignOutWithGoogleUseCase
import com.antsfamily.danskflashcards.domain.model.LanguageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val getAppVersionUseCase: GetAppVersionUseCase,
    private val getSelectedLanguageUseCase: GetSelectedLanguageUseCase,
    private val signOutWithGoogleUseCase: SignOutWithGoogleUseCase,
) : ViewModel() {

    private val _showLogoutDialog = MutableSharedFlow<Unit>()
    val showLogoutDialog = _showLogoutDialog.asSharedFlow()

    private val _navigateToAuthFlow = MutableSharedFlow<Unit>()
    val navigateToAuthFlow = _navigateToAuthFlow.asSharedFlow()


    private val _state = MutableStateFlow<SettingsUiState>(SettingsUiState.Loading)
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

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

    fun onLanguageClick() {

    }

    fun onNewLanguageSelected(type: LanguageType) {

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
        _state.value = SettingsUiState.Content(username, language, version)
    }

    private fun handleErrorState(e: Exception) {
        _state.value = SettingsUiState.Error(e.mapToErrorType())
    }
}