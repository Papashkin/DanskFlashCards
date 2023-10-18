package com.antsfamily.danskflashcards.ui.home

sealed class HomeUiState {
    object Loading: HomeUiState()
    data class Content(val content: List<String>): HomeUiState()
    data class Error(val errorMessage: String): HomeUiState()
}