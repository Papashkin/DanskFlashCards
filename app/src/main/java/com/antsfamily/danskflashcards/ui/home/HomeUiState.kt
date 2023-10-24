package com.antsfamily.danskflashcards.ui.home

import com.antsfamily.danskflashcards.data.WordModel

sealed class HomeUiState {
    data object Loading: HomeUiState()
    data class Content(
        val danish: List<WordModel> = emptyList(),
        val english: List<WordModel> = emptyList(),
        val totalCountdownTime: Long = 60L,
        val remainingCountdownTime: Long = 60L,
    ): HomeUiState()
    data class Error(val errorMessage: String): HomeUiState()
}