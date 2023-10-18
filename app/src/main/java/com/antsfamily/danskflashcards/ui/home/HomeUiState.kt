package com.antsfamily.danskflashcards.ui.home

import com.antsfamily.danskflashcards.data.Word

sealed class HomeUiState {
    object Loading: HomeUiState()
    data class Content(val content: List<Word>): HomeUiState()
    data class Error(val errorMessage: String): HomeUiState()
}