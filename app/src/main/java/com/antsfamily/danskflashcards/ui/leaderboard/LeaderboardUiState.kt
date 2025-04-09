package com.antsfamily.danskflashcards.ui.leaderboard

import com.antsfamily.danskflashcards.core.model.ErrorType
import com.antsfamily.danskflashcards.ui.home.model.LeaderItem

sealed class LeaderboardUiState {
    data object Loading: LeaderboardUiState()
    data class Content(
        val first: LeaderItem,
        val second: LeaderItem,
        val third: LeaderItem,
        val others: List<LeaderItem>
    ): LeaderboardUiState()
    data class Error(val type: ErrorType): LeaderboardUiState()
}