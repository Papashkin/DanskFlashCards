package com.antsfamily.danskflashcards.ui.home

import com.antsfamily.danskflashcards.ui.home.model.LeaderboardModel
import com.antsfamily.danskflashcards.ui.home.model.UserModel

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Content(
        val user: UserModel,
        val leaderboard: LeaderboardModel?,
        val cardsSize: Int
    ) : HomeUiState()

    data class Error(val errorMessage: String) : HomeUiState()
}