package com.antsfamily.danskflashcards.ui.auth

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.core.model.CurrentUserItem
import com.antsfamily.danskflashcards.core.model.mapToItem
import com.antsfamily.danskflashcards.data.GoogleAuthUiClient
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
    private val client: GoogleAuthUiClient
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
        val intentSender = client.getSignInIntentSender()
        intentSender?.let {
            _signInFlow.emit(it)
        } ?: run {
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
        val result = client.signInWithIntent(intent = intent ?: return@launch)
        val userModel = result.data?.mapToItem()
        if (userModel?.isValid() == true) {
            proceedWithUserData(userModel)
        }
    }

    private fun proceedWithUserData(model: CurrentUserItem) = viewModelScope.launch {
        setDefaultUiState()
        _navigationFlow.emit(model)
    }

    private fun setDefaultUiState() {
        _state.value = AuthUiState.Default
    }
}
