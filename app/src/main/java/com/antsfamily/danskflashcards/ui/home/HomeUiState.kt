package com.antsfamily.danskflashcards.ui.home

import com.antsfamily.danskflashcards.core.model.ErrorType
import com.antsfamily.danskflashcards.core.model.UserItem

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Content(
        val user: UserItem,
        val leaderboard: List<UserItem>,
    ) : HomeUiState()

    data class Error(val type: ErrorType) : HomeUiState()
}