package com.antsfamily.danskflashcards.ui.auth

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.core.model.CurrentUserItem
import com.antsfamily.danskflashcards.core.model.mapToItem
import com.antsfamily.danskflashcards.domain.GetSignInResultUseCase
import com.antsfamily.danskflashcards.domain.SignInWithGoogleUseCase
import com.google.android.gms.auth.api.identity.SignInClient
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
    private val getSignInResultUseCase: GetSignInResultUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val client: SignInClient,
) : ViewModel() {

    private val _signInFlow = MutableSharedFlow<IntentSender>()
    val signInFlow: SharedFlow<IntentSender> = _signInFlow.asSharedFlow()

    private val _navigationFlow = MutableSharedFlow<CurrentUserItem>()
    val navigationFlow: SharedFlow<CurrentUserItem> = _navigationFlow.asSharedFlow()

    private val _state = MutableStateFlow<AuthUiState>(AuthUiState.Default)
    val state: StateFlow<AuthUiState>
        get() = _state.asStateFlow()

    fun onGoogleClick() = viewModelScope.launch {
        _state.value = AuthUiState.Loading
        try {
            val intentSender = getSignInResultUseCase()?.pendingIntent?.intentSender
            intentSender?.let {
                _signInFlow.emit(it)
            } ?: run {
                setDefaultUiState()
            }
        } catch (e: Exception) {
            setDefaultUiState()
        }
    }

    fun handleSignInResult(code: Int, intent: Intent?) {
        if (code == Activity.RESULT_OK) {
            signIn(intent)
        } else {
            setDefaultUiState()
        }
    }

    private fun signIn(intent: Intent?) = viewModelScope.launch {
        val credential = client.getSignInCredentialFromIntent(intent)
        val googleToken = credential.googleIdToken
        val result = signInWithGoogleUseCase(googleToken ?: return@launch)
        val userModel = result.data?.mapToItem()
        if (userModel?.isValid() == true) {
            proceedWithUserData(userModel)
        }
    }

    private suspend fun proceedWithUserData(model: CurrentUserItem) {
        setDefaultUiState()
        _navigationFlow.emit(model)
    }

    private fun setDefaultUiState() {
        _state.value = AuthUiState.Default
    }
}
