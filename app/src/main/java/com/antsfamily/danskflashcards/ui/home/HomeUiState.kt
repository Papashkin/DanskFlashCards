package com.antsfamily.danskflashcards.ui.home

import com.antsfamily.danskflashcards.ui.home.model.UserModel

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Content(val user: UserModel, val cardsSize: Int) : HomeUiState()
    data class Error(val errorMessage: String) : HomeUiState()
}