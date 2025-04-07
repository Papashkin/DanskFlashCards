package com.antsfamily.danskflashcards.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.core.model.CurrentUserItem
import com.antsfamily.danskflashcards.domain.usecase.GetLoggedInUserUseCase
import com.antsfamily.danskflashcards.domain.usecase.IsOnboardingPassedUseCase
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.model.AppUpdateType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appUpdateManager: AppUpdateManager,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val isOnboardingPassedUseCase: IsOnboardingPassedUseCase,
) : ViewModel() {

    private val _navigationToHomeFlow = MutableSharedFlow<CurrentUserItem>()
    val navigationToHomeFlow: SharedFlow<CurrentUserItem> = _navigationToHomeFlow.asSharedFlow()

    private val _navigationToOnboardingFlow = MutableSharedFlow<CurrentUserItem>()
    val navigationToOnboardingFlow: SharedFlow<CurrentUserItem> =
        _navigationToOnboardingFlow.asSharedFlow()

    private val _startAppUpdateFlow = MutableSharedFlow<AppUpdateInfo>()
    val startAppUpdateFlow: SharedFlow<AppUpdateInfo> = _startAppUpdateFlow.asSharedFlow()

    private val _navigationToAuthFlow = MutableSharedFlow<Unit>()
    val navigationToAuthFlow: SharedFlow<Unit> = _navigationToAuthFlow.asSharedFlow()

    private val _updateAvailabilityFlow = MutableSharedFlow<Boolean>()
    val updateAvailabilityFlow: SharedFlow<Boolean> = _updateAvailabilityFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            checkForUpdates()
        }
    }

    fun onDismissClick() = viewModelScope.launch {
        checkIsUserLoggedIn()
    }

    fun onUpdateClick() = viewModelScope.launch {
        startUpdate()
    }

    private suspend fun startUpdate() {
        try {
            val data = appUpdateManager.appUpdateInfo.await()
            _startAppUpdateFlow.emit(data)
        } catch (e: Exception) {
            logError(e)
            checkIsUserLoggedIn()
        }
    }

    private suspend fun checkForUpdates() {
        try {
            val updateInfo = appUpdateManager.appUpdateInfo.await()
            if (updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                _updateAvailabilityFlow.emit(true)
            }
        } catch (e: Exception) {
            logError(e)
        } finally {
            checkIsUserLoggedIn()
        }
    }

    private fun logError(e: Exception) {
        Log.e(this::class.simpleName, e.toString())
    }

    private suspend fun checkIsUserLoggedIn() {
        val loggedInUser = getLoggedInUserUseCase()
        loggedInUser?.let {
            val userItem = CurrentUserItem(userId = it.id, username = it.username)
            checkOnboardingPass(userItem)
        } ?: run {
            _navigationToAuthFlow.emit(Unit)
        }
    }

    private suspend fun checkOnboardingPass(item: CurrentUserItem) {
        val isOnboardingPassed = isOnboardingPassedUseCase()
        if (isOnboardingPassed) {
            _navigationToHomeFlow.emit(item)
        } else {
            _navigationToOnboardingFlow.emit(item)
        }
    }
}