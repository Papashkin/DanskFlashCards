package com.antsfamily.danskflashcards.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.domain.GetAppUpdateInfoUseCase
import com.antsfamily.danskflashcards.domain.StartAppUpdateUseCase
import com.google.android.play.core.appupdate.AppUpdateInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SPLASH_DURATION_DELAY = 1000L

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getAppUpdateInfoUseCase: GetAppUpdateInfoUseCase,
    private val startAppUpdateUseCase: StartAppUpdateUseCase
) : ViewModel() {

    private val _navigationToAuthFlow = MutableSharedFlow<Unit>()
    val navigationToAuthFlow: SharedFlow<Unit> = _navigationToAuthFlow.asSharedFlow()

    private val _updateAvailabilityFlow = MutableSharedFlow<Boolean>()
    val updateAvailabilityFlow: SharedFlow<Boolean> = _updateAvailabilityFlow.asSharedFlow()

    private var updateInfo: AppUpdateInfo? = null

    init {
        viewModelScope.launch {
            checkForUpdates()
        }
    }

    fun onDismissClick() = viewModelScope.launch {
        navigateToAuth()
    }

    fun onUpdateClick() = viewModelScope.launch {
        updateInfo?.let {
            startAppUpdateUseCase(it)
        } ?: run {
            navigateToAuth()
        }
    }

    private suspend fun checkForUpdates() {
        val updateInfo = getAppUpdateInfoUseCase()
        updateInfo?.let {
            this.updateInfo = it
            _updateAvailabilityFlow.emit(true)
        } ?: run {
            navigateToAuth()
        }
    }

    private suspend fun navigateToAuth() {
        delay(SPLASH_DURATION_DELAY)
        _navigationToAuthFlow.emit(Unit)
    }
}