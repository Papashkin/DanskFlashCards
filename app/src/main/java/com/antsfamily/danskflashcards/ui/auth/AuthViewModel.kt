package com.antsfamily.danskflashcards.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.core.model.CurrentUserItem
import com.antsfamily.danskflashcards.core.model.ErrorType
import com.antsfamily.danskflashcards.core.model.mapToErrorType
import com.antsfamily.danskflashcards.core.model.mapToItem
import com.antsfamily.danskflashcards.data.SignInResult
import com.antsfamily.danskflashcards.data.model.SignInType
import com.antsfamily.danskflashcards.domain.IsOnboardingPassedUseCase
import com.antsfamily.danskflashcards.domain.SignInWithCredentialsUseCase
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
class AuthViewModel @Inject constructor(
    private val signInWithCredentialsUseCase: SignInWithCredentialsUseCase,
    private val isOnboardingPassedUseCase: IsOnboardingPassedUseCase,
) : ViewModel() {

    private val _navigationToHomeFlow = MutableSharedFlow<CurrentUserItem>()
    val navigationToHomeFlow: SharedFlow<CurrentUserItem> = _navigationToHomeFlow.asSharedFlow()

    private val _navigationToOnboardingFlow = MutableSharedFlow<CurrentUserItem>()
    val navigationToOnboardingFlow: SharedFlow<CurrentUserItem> =
        _navigationToOnboardingFlow.asSharedFlow()

    private val _state = MutableStateFlow<AuthUiState>(AuthUiState.Default)
    val state: StateFlow<AuthUiState>
        get() = _state.asStateFlow()

    fun onGoogleClick() = viewModelScope.launch {
        _state.value = AuthUiState.Loading
        try {
            val response = signInWithCredentialsUseCase(SignInType.GOOGLE)
            response?.let {
                proceedWithSignedUser(it)
            } ?: run {
                setDefaultUiState()
            }
        } catch (e: Exception) {
            proceedWithError(e.mapToErrorType())
        }
    }

    private suspend fun proceedWithSignedUser(result: SignInResult) {
        val userModel = result.data?.mapToItem()
        if (userModel?.isValid() == true) {
            proceedWithUserData(userModel)
        }
    }

    private fun proceedWithError(errorType: ErrorType) {
        _state.value = AuthUiState.Error(errorType)
    }

    private suspend fun proceedWithUserData(model: CurrentUserItem) {
        val isOnboardingPassed = isOnboardingPassedUseCase()
        if (isOnboardingPassed) {
            _navigationToHomeFlow.emit(model)
        } else {
            _navigationToOnboardingFlow.emit(model)
        }
        setDefaultUiState()
    }

    private fun setDefaultUiState() {
        _state.value = AuthUiState.Default
    }
}
