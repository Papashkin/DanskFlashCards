package com.antsfamily.danskflashcards.ui.about

sealed class AboutUiState {
    data class Content(val version: String?): AboutUiState()
}