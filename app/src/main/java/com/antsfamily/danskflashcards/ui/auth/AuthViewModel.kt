package com.antsfamily.danskflashcards.ui.auth

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.data.GoogleAuthUiClient
import com.antsfamily.danskflashcards.data.UserData
import com.antsfamily.danskflashcards.navigation.Screen
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

    private val _navigationFlow = MutableSharedFlow<String>()
    val navigationFlow: SharedFlow<String> = _navigationFlow.asSharedFlow()

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
            cancelSignIn()
        }
    }

    private fun signIn(intent: Intent?) = viewModelScope.launch {
        val result = client.signInWithIntent(intent = intent ?: return@launch)
        result.data?.let {
            proceedWithUserData(it)
        }
    }

    private fun cancelSignIn() {
        setDefaultUiState()
    }

    private fun proceedWithUserData(userData: UserData) = viewModelScope.launch {
        val navigationRoute = "${Screen.Home.route.substringBefore("/")}/${userData.username}"
        _navigationFlow.emit(navigationRoute)
    }

    private fun setDefaultUiState() {
        _state.value = AuthUiState.Default
    }
}
