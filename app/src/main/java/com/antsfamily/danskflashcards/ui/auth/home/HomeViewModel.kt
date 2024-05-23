package com.antsfamily.danskflashcards.ui.auth.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.danskflashcards.data.WordApiModel
import com.antsfamily.danskflashcards.data.mapToModel
import com.antsfamily.danskflashcards.domain.FetchDataUseCase
import com.antsfamily.danskflashcards.domain.GetResultUseCase
import com.antsfamily.danskflashcards.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val fetchDataUseCase: FetchDataUseCase,
    private val getResultUseCase: GetResultUseCase,
) : ViewModel() {

    private var username: String? = null
    private var words = emptyList<WordApiModel?>()
    private var percent: Float = 0.0f

    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state: StateFlow<HomeUiState>
        get() = _state.asStateFlow()

    private val _navigationFlow = MutableSharedFlow<String>()
    val navigationFlow: SharedFlow<String> = _navigationFlow.asSharedFlow()

    fun init(username: String) {
        _state.value = HomeUiState.Loading
        this.username = username
        getData(username)
    }

    fun onStartClick() = viewModelScope.launch {
        _navigationFlow.emit(Screen.Game.route)
    }

    private fun getData(username: String) = viewModelScope.launch {
        try {
            val result = getResultUseCase(Unit).firstOrNull()
            fetchDataUseCase(Unit) { data ->
                words = data
                _state.value = HomeUiState.Content(
                    userName = username,
                    personalBest =result.mapToModel(data.filterNotNull().size),
                )
            }

        } catch (e: Exception) {
            _state.value = HomeUiState.Error(errorMessage = e.message.orEmpty())
        }
    }
}
