package com.antsfamily.danskflashcards.ui.home

import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.data.GoogleAuthUiClient
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
class HomeViewModel @Inject constructor(
    private val client: GoogleAuthUiClient
) : ViewModel() {

    private val _signInFlow = MutableSharedFlow<IntentSender>()
    val signInFlow: SharedFlow<IntentSender> = _signInFlow.asSharedFlow()

    private val _navigationFlow = MutableSharedFlow<String>()
    val navigationFlow: SharedFlow<String> = _navigationFlow.asSharedFlow()

    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Default)
    val state: StateFlow<HomeUiState>
        get() = _state.asStateFlow()

    fun onGoogleClick() = viewModelScope.launch {
        _state.value = HomeUiState.Loading

        val intentSender = client.signIn()
        intentSender?.let {
            _signInFlow.emit(it)
        } ?: run {
            _state.value = HomeUiState.Default
        }
    }

    fun signIn(intent: Intent?) = viewModelScope.launch {
        val result = client.signInWithIntent(
            intent = intent ?: return@launch
        )
        result.data?.let {
            _navigationFlow.emit(Screen.Game.route)
        }
    }

    fun cancelSignIn() {
        _state.value = HomeUiState.Default
    }
}
