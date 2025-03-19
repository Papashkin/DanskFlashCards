package com.antsfamily.danskflashcards.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.core.model.CurrentUserItem
import com.antsfamily.danskflashcards.core.model.ErrorType
import com.antsfamily.danskflashcards.core.model.mapToErrorType
import com.antsfamily.danskflashcards.core.model.mapToItem
import com.antsfamily.danskflashcards.data.SignInResult
import com.antsfamily.danskflashcards.data.model.SignInType
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
) : ViewModel() {

    private val _navigationFlow = MutableSharedFlow<CurrentUserItem>()
    val navigationFlow: SharedFlow<CurrentUserItem> = _navigationFlow.asSharedFlow()

    private val _state = MutableStateFlow<AuthUiState>(AuthUiState.Default)
    val state: StateFlow<AuthUiState>
        get() = _state.asStateFlow()

    fun onGoogleClick() = viewModelScope.launch {
        _state.value = AuthUiState.Loading
        try {
            val response = signInWithCredentialsUseCase(SignInType.GOOGLE)
            proceedWithSignedUser(response)
        }catch (e: Exception) {
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
        setDefaultUiState()
        _navigationFlow.emit(model)
    }

    private fun setDefaultUiState() {
        _state.value = AuthUiState.Default
    }
}
