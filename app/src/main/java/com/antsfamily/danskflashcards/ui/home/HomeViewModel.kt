package com.antsfamily.danskflashcards.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.core.model.CurrentUserItem
import com.antsfamily.danskflashcards.core.model.mapToErrorType
import com.antsfamily.danskflashcards.core.util.orZero
import com.antsfamily.danskflashcards.domain.GetUsersUseCase
import com.antsfamily.danskflashcards.domain.GetWordsAmountUseCase
import com.antsfamily.danskflashcards.domain.SignOutWithGoogleUseCase
import com.antsfamily.danskflashcards.domain.UserUpdateFLowUseCase
import com.antsfamily.danskflashcards.domain.model.UserDomain
import com.antsfamily.danskflashcards.ui.home.model.LeaderItem
import com.antsfamily.danskflashcards.ui.home.model.LeaderboardItem
import com.antsfamily.danskflashcards.ui.home.model.UserItem
import com.antsfamily.danskflashcards.ui.home.model.toItem
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
    private val getWordsAmountUseCase: GetWordsAmountUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val userUpdateFLowUseCase: UserUpdateFLowUseCase,
    private val signOutWithGoogleUseCase: SignOutWithGoogleUseCase,
    @Assisted("user") private val user: CurrentUserItem
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("user") user: CurrentUserItem): HomeViewModel
    }

    private var wordsAmount: Int? = null
    private var currentUser: UserItem? = null

    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state: StateFlow<HomeUiState>
        get() = _state.asStateFlow()

    private val _navigationToGameFlow = MutableSharedFlow<Int>()
    val navigationToGameFlow: SharedFlow<Int> = _navigationToGameFlow.asSharedFlow()

    private val _navigationBackFlow = MutableSharedFlow<Unit>()
    val navigationBackFlow: SharedFlow<Unit> = _navigationBackFlow.asSharedFlow()

    init {
        getCards()
    }

    fun onRetryClick() {
        getCards()
    }

    fun onStartClick() = viewModelScope.launch(Dispatchers.IO) {
        _navigationToGameFlow.emit(currentUser?.score.orZero())
    }

    fun onBackButtonClick() = viewModelScope.launch(Dispatchers.IO) {
        try {
            _state.value = HomeUiState.Loading
            signOutWithGoogleUseCase()
        } catch (e: Exception) {
            // no-op
        } finally {
            _navigationBackFlow.emit(Unit)
        }
    }

    private fun getCards() {
        if (wordsAmount == null) {
            getWords()
        } else {
            getUsers(user)
        }
    }

    private fun getWords() = viewModelScope.launch(Dispatchers.IO) {
        try {
            wordsAmount = getWordsAmountUseCase()
            getUsers(user)
        } catch (e: Exception) {
            onGetUsersErrorResult(e)
        }
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
        val users = data.map { it.toItem(user.userId) }
        currentUser = users.firstOrNull { it.isCurrentUser }
        updateState(users, currentUser ?: user.mapToUserItem())
    }

    private fun updateState(users: List<UserItem>, user: UserItem) {
        val model = getLeaderboard(users, user)
        _state.value =
            HomeUiState.Content(user = user, cardsSize = wordsAmount.orZero(), leaderboard = model)
    }

    private fun getLeaderboard(users: List<UserItem>, user: UserItem): LeaderboardItem {
        val sortedUsers = users.sortedByDescending { it.score }
        val leaderItems = sortedUsers
            .take(3)
            .mapIndexed { index, sortedUser ->
                LeaderItem(
                    name = sortedUser.name,
                    surname = sortedUser.surname,
                    index = index,
                    score = sortedUser.score
                )
            }
        return LeaderboardItem(
            leaders = leaderItems,
            user = LeaderItem(
                name = user.name,
                surname = user.surname,
                index = sortedUsers.indexOfFirst { it.isCurrentUser },
                score = user.score
            )
        )
    }

    private fun onGetUsersErrorResult(e: Exception) = viewModelScope.launch {
        _state.value = HomeUiState.Error(e.mapToErrorType())
    }
}
