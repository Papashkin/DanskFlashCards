package com.antsfamily.danskflashcards.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.data.GoogleAuthUiClient
import com.antsfamily.danskflashcards.data.model.WordApiModel
import com.antsfamily.danskflashcards.domain.GetFlashCardsUseCase
import com.antsfamily.danskflashcards.domain.GetUsersUseCase
import com.antsfamily.danskflashcards.ui.auth.CurrentUserModel
import com.antsfamily.danskflashcards.ui.home.model.UserModel
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
    private val client: GoogleAuthUiClient,
    @Assisted("user") private val user: CurrentUserModel
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("user") user: CurrentUserModel): HomeViewModel
    }

    private var words = emptyList<WordApiModel?>()
    private var percent: Float = 0.0f

    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state: StateFlow<HomeUiState>
        get() = _state.asStateFlow()

    private val _navigationToGameFlow = MutableSharedFlow<Unit>()
    val navigationToGameFlow: SharedFlow<Unit> = _navigationToGameFlow.asSharedFlow()

    private val _navigationBackFlow = MutableSharedFlow<Unit>()
    val navigationBackFlow: SharedFlow<Unit> = _navigationBackFlow.asSharedFlow()

    init {
        getUsers(user)
    }

    private fun getUsers(user: CurrentUserModel) = viewModelScope.launch(Dispatchers.IO) {
        try {
            getUsersUseCase(user.userId) { data ->
                val users = data.map { it.toModel(user.userId) }
                val currentUser = users.firstOrNull { it.isCurrentUser }
                onGetUsersSuccessResult(users, currentUser ?: user.mapToUserModel())
            }
        } catch (e: Exception) {
            onGetUsersErrorResult(e)
        }
    }

    fun onStartClick() = viewModelScope.launch(Dispatchers.IO) {
        _navigationToGameFlow.emit(Unit)
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

    private fun onGetUsersSuccessResult(users: List<UserModel>, currentUser: UserModel) = viewModelScope.launch {
        try {
            getFlashCardsUseCase(Unit) { data ->
                words = data
                _state.value = HomeUiState.Content(user = currentUser, cardsSize = data.size)
            }
        } catch (e: Exception) {
            _state.value = HomeUiState.Error(errorMessage = e.message.orEmpty())
        }
    }

    private fun onGetUsersErrorResult(e: Exception) = viewModelScope.launch {
        _state.value = HomeUiState.Error(errorMessage = e.message.orEmpty())
    }
}
