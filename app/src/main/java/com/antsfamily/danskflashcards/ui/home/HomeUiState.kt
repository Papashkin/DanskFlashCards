package com.antsfamily.danskflashcards.ui.home

import com.antsfamily.danskflashcards.data.PersonalBest

sealed class HomeUiState {
    data object Loading: HomeUiState()
    data class Content(
        val userName: String,
        val personalBest: PersonalBest
    ): HomeUiState()
    data class Error(val errorMessage: String): HomeUiState()
}