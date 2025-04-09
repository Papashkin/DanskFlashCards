package com.antsfamily.danskflashcards.ui.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.core.model.ErrorType
import com.antsfamily.danskflashcards.core.model.mapToErrorType
import com.antsfamily.danskflashcards.core.navigation.NAVIGATION_ANIMATION_DURATION
import com.antsfamily.danskflashcards.domain.model.UserDomain
import com.antsfamily.danskflashcards.domain.usecase.GetLoggedInUserUseCase
import com.antsfamily.danskflashcards.domain.usecase.GetUsersUseCase
import com.antsfamily.danskflashcards.core.model.toItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<LeaderboardUiState>(LeaderboardUiState.Loading)
    val state: StateFlow<LeaderboardUiState>
        get() = _state.asStateFlow()

    init {
        getUsers()
    }

    fun onRetryClick() {
        _state.value = LeaderboardUiState.Loading
        getUsers()
    }

    private fun getUsers() = viewModelScope.launch {
        delay(NAVIGATION_ANIMATION_DURATION.toLong())
        try {
            val currentUser = getLoggedInUserUseCase()
            currentUser?.let {
                val data = getUsersUseCase(it.id)
                handleSuccessResult(data)
            } ?: run {
                _state.value = LeaderboardUiState.Error(ErrorType.Unknown)
            }
        } catch (e: Exception) {
            _state.value = LeaderboardUiState.Error(e.mapToErrorType())
        }
    }

    private fun handleSuccessResult(data: List<UserDomain>) {
        val leaderItems = data
            .sortedByDescending { it.score }
            .mapIndexed { index, sortedUser -> sortedUser.toItem(index) }

        _state.value = LeaderboardUiState.Content(
            first = leaderItems.first(),
            second = leaderItems[1],
            third = leaderItems[2],
            others = leaderItems.drop(3)
        )
    }
}