package com.antsfamily.danskflashcards.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SPLASH_DURATION_DELAY = 1200L

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {


    private val _navigationFlow = MutableSharedFlow<String>()
    val navigationFlow: SharedFlow<String> = _navigationFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            delay(SPLASH_DURATION_DELAY)
            _navigationFlow.emit(Screen.Auth.route)
        }
    }
}