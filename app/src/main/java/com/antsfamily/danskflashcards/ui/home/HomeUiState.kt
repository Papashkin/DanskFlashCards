package com.antsfamily.danskflashcards.ui.home

import com.antsfamily.danskflashcards.data.GameStatus
import com.antsfamily.danskflashcards.data.WordModel
import com.antsfamily.danskflashcards.util.COUNTDOWN_TIME_SEC

sealed class HomeUiState {
    data object Loading: HomeUiState()
    data class Content(
        val danish: List<WordModel> = emptyList(),
        val english: List<WordModel> = emptyList(),
        val totalCountdownTime: Long = COUNTDOWN_TIME_SEC,
        val remainingCountdownTime: Long = COUNTDOWN_TIME_SEC,
        val timerProgress: Float = 1f,
        val status: GameStatus = GameStatus.READY,
    ): HomeUiState()
    data class Error(val errorMessage: String): HomeUiState()
}