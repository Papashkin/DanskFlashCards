package com.antsfamily.danskflashcards.ui.home

import com.antsfamily.danskflashcards.data.WordModel

sealed class HomeUiState {
    data object Loading: HomeUiState()
    data class Content(val danish: List<WordModel>, val english: List<WordModel>): HomeUiState()
    data class Error(val errorMessage: String): HomeUiState()
}