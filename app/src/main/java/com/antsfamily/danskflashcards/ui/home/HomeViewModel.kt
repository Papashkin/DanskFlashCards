package com.antsfamily.danskflashcards.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.data.GoogleAuthUiClient
import com.antsfamily.danskflashcards.data.model.WordApiModel
import com.antsfamily.danskflashcards.data.model.mapToModel
import com.antsfamily.danskflashcards.domain.GetFlashCardsUseCase
import com.antsfamily.danskflashcards.domain.GetPersonalBestUseCase
import com.antsfamily.danskflashcards.navigation.Screen
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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFlashCardsUseCase: GetFlashCardsUseCase,
    private val getPersonalBestUseCase: GetPersonalBestUseCase,
    private val client: GoogleAuthUiClient
) : ViewModel() {

    private var username: String? = null
    private var words = emptyList<WordApiModel?>()
    private var percent: Float = 0.0f

    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state: StateFlow<HomeUiState>
        get() = _state.asStateFlow()

    private val _navigationFlow = MutableSharedFlow<String>()
    val navigationFlow: SharedFlow<String> = _navigationFlow.asSharedFlow()


    private val _navigationBackFlow = MutableSharedFlow<Unit>()
    val navigationBackFlow: SharedFlow<Unit> = _navigationBackFlow.asSharedFlow()

    fun init(username: String) {
        _state.value = HomeUiState.Loading
        this.username = username
        getData(username)
    }

    fun onStartClick() = viewModelScope.launch {
        _navigationFlow.emit(Screen.Game.route)
    }

    fun onBackButtonClick() = viewModelScope.launch {
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
