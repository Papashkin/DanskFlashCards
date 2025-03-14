package com.antsfamily.danskflashcards.ui.home

import com.antsfamily.danskflashcards.core.model.ErrorType
import com.antsfamily.danskflashcards.ui.home.model.LeaderboardItem
import com.antsfamily.danskflashcards.ui.home.model.UserItem

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Content(
        val user: UserItem,
        val leaderboard: LeaderboardItem?,
        val cardsSize: Int
    ) : HomeUiState()

    data class Error(val type: ErrorType) : HomeUiState()
}