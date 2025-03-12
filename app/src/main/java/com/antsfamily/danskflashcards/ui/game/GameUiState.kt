package com.antsfamily.danskflashcards.ui.game

import com.antsfamily.danskflashcards.core.model.ErrorType
import com.antsfamily.danskflashcards.ui.game.model.GameStatus
import com.antsfamily.danskflashcards.ui.game.model.TimerModel
import com.antsfamily.danskflashcards.ui.game.model.WordModel

sealed class GameUiState {
    data object Loading: GameUiState()
    data class Content(
        val danish: List<WordModel> = emptyList(),
        val english: List<WordModel> = emptyList(),
        val timerModel: TimerModel = TimerModel(),
        val status: GameStatus = GameStatus.READY,
    ): GameUiState()
    data class Error(val type: ErrorType): GameUiState()
}