package com.antsfamily.danskflashcards.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.data.GoogleAuthUiClient
import com.antsfamily.danskflashcards.data.model.UserApiModel
import com.antsfamily.danskflashcards.data.model.WordApiModel
import com.antsfamily.danskflashcards.domain.GetFlashCardsUseCase
import com.antsfamily.danskflashcards.domain.GetUsersUseCase
import com.antsfamily.danskflashcards.domain.UserUpdateFLowUseCase
import com.antsfamily.danskflashcards.ui.auth.CurrentUserModel
import com.antsfamily.danskflashcards.ui.home.model.LeaderboardItem
import com.antsfamily.danskflashcards.ui.home.model.LeaderboardModel
import com.antsfamily.danskflashcards.ui.home.model.UserModel
import com.antsfamily.danskflashcards.ui.home.model.toModel
import com.antsfamily.danskflashcards.core.util.orZero
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
    private val getFlashCardsUseCase: GetFlashCardsUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val userUpdateFLowUseCase: UserUpdateFLowUseCase,
    private val client: GoogleAuthUiClient,
    @Assisted("user") private val user: CurrentUserModel
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("user") user: CurrentUserModel): HomeViewModel
    }

    private var words = emptyList<WordApiModel?>()
    private var currentUser: UserModel? = null

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

    fun onStartClick() = viewModelScope.launch(Dispatchers.IO) {
        _navigationToGameFlow.emit(currentUser?.score.orZero())
    }

    fun onBackButtonClick() = viewModelScope.launch(Dispatchers.IO) {
        try {
            _state.value = HomeUiState.Loading
            client.signOut()
        } catch (e: Exception) {
            // no-op
        } finally {
            _navigationBackFlow.emit(Unit)
        }
    }

    private fun getCards() = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (words.isEmpty()) {
                words = getFlashCardsUseCase.run()
            }
            getUsers(user)
        } catch (e: Exception) {
            _state.value = HomeUiState.Error(errorMessage = e.message.orEmpty())
        }
    }

    private fun getUsers(user: CurrentUserModel) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val data = getUsersUseCase.run(user.userId)
            onGetUsersSuccessResult(data)
        } catch (e: Exception) {
            onGetUsersErrorResult(e)
        } finally {
            subscribeToUserUpdate(user.userId)
        }
    }

    private fun subscribeToUserUpdate(userId: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            userUpdateFLowUseCase.run(userId).collect { data ->
                onGetUsersSuccessResult(data)
            }
        } catch (e: Exception) {
            onGetUsersErrorResult(e)
        }
    }

    private fun onGetUsersSuccessResult(data: List<UserApiModel>) {
        val users = data.map { it.toModel(user.userId) }
        currentUser = users.firstOrNull { it.isCurrentUser }
        updateState(users, currentUser ?: user.mapToUserModel())
    }

    private fun updateState(users: List<UserModel>, user: UserModel) {
        val model = getLeaderboard(users, user)
        _state.value = HomeUiState.Content(user = user, cardsSize = words.size, leaderboard = model)
    }

    private fun getLeaderboard(users: List<UserModel>, user: UserModel): LeaderboardModel {
        val sortedUsers = users.sortedByDescending { it.score }
        val leaderboardItems = sortedUsers
            .take(3)
            .mapIndexed { index, sortedUser ->
                LeaderboardItem(name = sortedUser.username, index = index, score = sortedUser.score)
            }
        return LeaderboardModel(
            leaders = leaderboardItems,
            user = LeaderboardItem(
                name = user.username,
                index = sortedUsers.indexOfFirst { it.isCurrentUser },
                score = user.score
            )
        )
    }

    private fun onGetUsersErrorResult(e: Exception) = viewModelScope.launch {
        _state.value = HomeUiState.Error(errorMessage = e.message.orEmpty())
    }
}
