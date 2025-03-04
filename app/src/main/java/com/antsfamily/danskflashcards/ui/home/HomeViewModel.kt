package com.antsfamily.danskflashcards.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.data.GoogleAuthUiClient
import com.antsfamily.danskflashcards.data.model.WordApiModel
import com.antsfamily.danskflashcards.data.model.mapToModel
import com.antsfamily.danskflashcards.domain.GetFlashCardsUseCase
import com.antsfamily.danskflashcards.domain.GetPersonalBestUseCase
import com.antsfamily.danskflashcards.domain.GetUsersUseCase
import com.antsfamily.danskflashcards.navigation.Screen
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel(assistedFactory = HomeViewModel.Factory::class)
class HomeViewModel @AssistedInject constructor(
    private val getFlashCardsUseCase: GetFlashCardsUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val getPersonalBestUseCase: GetPersonalBestUseCase,
    private val client: GoogleAuthUiClient,
    @Assisted("userId") private val userId: String,
    @Assisted("username") private val username: String,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("userId") userId: String,
            @Assisted("username") username: String
        ): HomeViewModel
    }

    private var words = emptyList<WordApiModel?>()
    private var percent: Float = 0.0f

    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state: StateFlow<HomeUiState>
        get() = _state.asStateFlow()

    private val _navigationFlow = MutableSharedFlow<String>()
    val navigationFlow: SharedFlow<String> = _navigationFlow.asSharedFlow()

    private val _navigationBackFlow = MutableSharedFlow<Unit>()
    val navigationBackFlow: SharedFlow<Unit> = _navigationBackFlow.asSharedFlow()

    init {
        getUsers(username, userId)
    }

    private fun getUsers(username: String, userId: String) = viewModelScope.launch(Dispatchers.IO) {
        getUsersUseCase(userId) {
            println(it)
            getData(username)
        }
    }

    fun onStartClick() = viewModelScope.launch(Dispatchers.IO) {
        _navigationFlow.emit(Screen.Game.route)
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

    private fun getData(username: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val result = getPersonalBestUseCase(Unit).firstOrNull()
            getFlashCardsUseCase(Unit) { data ->
                words = data
                _state.value = HomeUiState.Content(
                    userName = username,
                    personalBest = result.mapToModel(data.size),
                )
            }
        } catch (e: Exception) {
            _state.value = HomeUiState.Error(errorMessage = e.message.orEmpty())
        }
    }
}
