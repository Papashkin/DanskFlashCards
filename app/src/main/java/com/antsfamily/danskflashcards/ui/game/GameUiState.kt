package com.antsfamily.danskflashcards.ui.game

import com.antsfamily.danskflashcards.ui.game.model.GameStatus
import com.antsfamily.danskflashcards.ui.game.model.WordModel
import com.antsfamily.danskflashcards.util.COUNTDOWN_TIME_SEC

sealed class GameUiState {
    data object Loading: GameUiState()
    data class Content(
        val danish: List<WordModel> = emptyList(),
        val english: List<WordModel> = emptyList(),
        val totalCountdownTime: Long = COUNTDOWN_TIME_SEC,
        val remainingCountdownTime: Long = COUNTDOWN_TIME_SEC,
        val timerProgress: Float = 1f,
        val status: GameStatus = GameStatus.READY,
    ): GameUiState()
    data class Error(val errorMessage: String): GameUiState()
}