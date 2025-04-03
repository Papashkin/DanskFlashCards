package com.antsfamily.danskflashcards.ui.game

import com.antsfamily.danskflashcards.core.model.ErrorType
import com.antsfamily.danskflashcards.ui.game.model.GameStatus
import com.antsfamily.danskflashcards.ui.game.model.TimerItem
import com.antsfamily.danskflashcards.ui.game.model.WordItem

sealed class GameUiState {
    data object Loading: GameUiState()
    data object Countdown: GameUiState()
    data class Content(
        val learningWords: List<WordItem> = emptyList(),
        val primaryWords: List<WordItem> = emptyList(),
        val timerItem: TimerItem = TimerItem(),
        val status: GameStatus = GameStatus.READY,
    ): GameUiState()
    data class Error(val type: ErrorType): GameUiState()
}