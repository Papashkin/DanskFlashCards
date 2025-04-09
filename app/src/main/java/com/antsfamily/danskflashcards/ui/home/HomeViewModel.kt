package com.antsfamily.danskflashcards.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.core.model.CurrentUserItem
import com.antsfamily.danskflashcards.core.model.mapToErrorType
import com.antsfamily.danskflashcards.core.util.orZero
import com.antsfamily.danskflashcards.domain.usecase.GetUsersUseCase
import com.antsfamily.danskflashcards.domain.usecase.UserUpdateFLowUseCase
import com.antsfamily.danskflashcards.domain.model.UserDomain
import com.antsfamily.danskflashcards.core.model.UserItem
import com.antsfamily.danskflashcards.core.model.toItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = HomeViewModel.Factory::class)
class HomeViewModel @AssistedInject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val userUpdateFLowUseCase: UserUpdateFLowUseCase,
    @Assisted("user") private val user: CurrentUserItem
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("user") user: CurrentUserItem): HomeViewModel
    }

    companion object {
        private const val LEADERBOARD_SIZE = 3
    }

    private var currentUser: UserItem? = null

    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state: StateFlow<HomeUiState>
        get() = _state.asStateFlow()

    private val _navigationToGameFlow = MutableSharedFlow<Int>()
    val navigationToGameFlow: SharedFlow<Int> = _navigationToGameFlow.asSharedFlow()

    init {
        getUsers(user)
    }

    fun onRetryClick() {
        getUsers(user)
    }

    fun onStartClick() = viewModelScope.launch(Dispatchers.IO) {
        _navigationToGameFlow.emit(currentUser?.score.orZero())
    }

    private fun getUsers(user: CurrentUserItem) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val data = getUsersUseCase(user.userId)
            onGetUsersSuccessResult(data)
        } catch (e: Exception) {
            onGetUsersErrorResult(e)
        } finally {
            subscribeToUserUpdate(user.userId)
        }
    }

    private fun subscribeToUserUpdate(userId: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            userUpdateFLowUseCase(userId).collect { data ->
                onGetUsersSuccessResult(data)
            }
        } catch (e: Exception) {
            // no-op
        }
    }

    private fun onGetUsersSuccessResult(data: List<UserDomain>) {
        val users = data
            .sortedByDescending { it.score }
            .mapIndexed { index, domainItem -> domainItem.toItem(index) }
        currentUser = users.firstOrNull { it.isCurrentUser }
        updateState(users, currentUser ?: user.mapToUserItem())
    }

    private fun updateState(users: List<UserItem>, user: UserItem) {
//        val leaderboardItems = getLeaderboard(users)
        _state.value = HomeUiState.Content(
            user = user,
            leaderboard = users.take(LEADERBOARD_SIZE),
//            userPlace = users.firstOrNull { it.isCurrentUser }?.place
        )
    }

//    private fun getLeaderboard(users: List<UserItem>): List<LeaderItem> {
//        val leaderItems = users
//            .sortedByDescending { it.score }
//            .mapIndexed { index, sortedUser -> sortedUser.toLeaderItem(index) }
//        return leaderItems
//    }

    private fun onGetUsersErrorResult(e: Exception) = viewModelScope.launch {
        _state.value = HomeUiState.Error(e.mapToErrorType())
    }
}
