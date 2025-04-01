package com.antsfamily.danskflashcards.ui.home

import com.antsfamily.danskflashcards.core.model.ErrorType
import com.antsfamily.danskflashcards.ui.home.model.LeaderItem
import com.antsfamily.danskflashcards.ui.home.model.UserItem

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Content(
        val user: UserItem,
        val leaderboard: List<LeaderItem>,
        val cardsSize: Int
    ) : HomeUiState()

    data class Error(val type: ErrorType) : HomeUiState()
}