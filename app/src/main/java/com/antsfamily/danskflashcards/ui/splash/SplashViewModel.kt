package com.antsfamily.danskflashcards.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.core.model.CurrentUserItem
import com.antsfamily.danskflashcards.core.model.mapToErrorType
import com.antsfamily.danskflashcards.domain.usecase.GetLoggedInUserUseCase
import com.antsfamily.danskflashcards.domain.usecase.IsOnboardingPassedUseCase
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.model.AppUpdateType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appUpdateManager: AppUpdateManager,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val isOnboardingPassedUseCase: IsOnboardingPassedUseCase,
) : ViewModel() {

    companion object {
        private const val UPDATES_TIMEOUT = 2000L
    }

    private val _navigationToHomeFlow = MutableSharedFlow<CurrentUserItem>()
    val navigationToHomeFlow: SharedFlow<CurrentUserItem> = _navigationToHomeFlow.asSharedFlow()

    private val _navigationToOnboardingFlow = MutableSharedFlow<CurrentUserItem>()
    val navigationToOnboardingFlow: SharedFlow<CurrentUserItem> =
        _navigationToOnboardingFlow.asSharedFlow()

    private val _startAppUpdateFlow = MutableSharedFlow<AppUpdateInfo>()
    val startAppUpdateFlow: SharedFlow<AppUpdateInfo> = _startAppUpdateFlow.asSharedFlow()

    private val _navigationToAuthFlow = MutableSharedFlow<Unit>()
    val navigationToAuthFlow: SharedFlow<Unit> = _navigationToAuthFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            checkForUpdates()
        }
    }

    private suspend fun checkForUpdates() {
        try {
            val updateInfo = withTimeout(UPDATES_TIMEOUT) {
                appUpdateManager.appUpdateInfo.await()
            }
            if (updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                _startAppUpdateFlow.emit(updateInfo)
            } else {
                checkIsUserLoggedIn()
            }
        } catch (e: TimeoutCancellationException) {
            logError(e)
            checkIsUserLoggedIn()
        } catch (e: Exception) {
            logError(e)
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