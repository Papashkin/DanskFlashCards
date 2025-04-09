package com.antsfamily.danskflashcards.ui.leaderboard

import com.antsfamily.danskflashcards.core.model.ErrorType
import com.antsfamily.danskflashcards.core.model.UserItem

sealed class LeaderboardUiState {
    data object Loading: LeaderboardUiState()
    data class Content(
        val first: UserItem,
        val second: UserItem,
        val third: UserItem,
        val others: List<UserItem>
    ): LeaderboardUiState()
    data class Error(val type: ErrorType): LeaderboardUiState()
}