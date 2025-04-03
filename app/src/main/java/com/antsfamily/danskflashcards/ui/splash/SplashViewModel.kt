package com.antsfamily.danskflashcards.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.core.model.CurrentUserItem
import com.antsfamily.danskflashcards.core.model.mapToItem
import com.antsfamily.danskflashcards.domain.GetAppUpdateInfoUseCase
import com.antsfamily.danskflashcards.domain.GetLoggedInUserUseCase
import com.antsfamily.danskflashcards.domain.IsOnboardingPassedUseCase
import com.antsfamily.danskflashcards.domain.StartAppUpdateUseCase
import com.google.android.play.core.appupdate.AppUpdateInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getAppUpdateInfoUseCase: GetAppUpdateInfoUseCase,
    private val startAppUpdateUseCase: StartAppUpdateUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val isOnboardingPassedUseCase: IsOnboardingPassedUseCase,
) : ViewModel() {

    private val _navigationToHomeFlow = MutableSharedFlow<CurrentUserItem>()
    val navigationToHomeFlow: SharedFlow<CurrentUserItem> = _navigationToHomeFlow.asSharedFlow()

    private val _navigationToOnboardingFlow = MutableSharedFlow<CurrentUserItem>()
    val navigationToOnboardingFlow: SharedFlow<CurrentUserItem> = _navigationToOnboardingFlow.asSharedFlow()

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
        checkIsUserLoggedIn()
    }

    fun onUpdateClick() = viewModelScope.launch {
        updateInfo?.let {
            startAppUpdateUseCase(it)
        } ?: run {
            checkIsUserLoggedIn()
        }
    }

    private suspend fun checkForUpdates() {
        val updateInfo = getAppUpdateInfoUseCase()
        updateInfo?.let {
            this.updateInfo = it
            _updateAvailabilityFlow.emit(true)
        } ?: run {
            checkIsUserLoggedIn()
        }
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