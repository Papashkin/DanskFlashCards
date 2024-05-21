package com.antsfamily.danskflashcards.ui.home

sealed class HomeUiState {
    data object Default: HomeUiState()
    data object Loading: HomeUiState()
    data class Error(val errorMessage: String): HomeUiState()
}