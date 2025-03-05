package com.antsfamily.danskflashcards.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SPLASH_DURATION_DELAY = 1000L

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    private val _navigationToAuthFlow = MutableSharedFlow<Unit>()
    val navigationToAuthFlow: SharedFlow<Unit> = _navigationToAuthFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            delay(SPLASH_DURATION_DELAY)
            _navigationToAuthFlow.emit(Unit)
        }
    }
}