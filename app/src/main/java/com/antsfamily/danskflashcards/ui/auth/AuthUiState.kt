package com.antsfamily.danskflashcards.ui.auth

sealed class AuthUiState {
    data object Default: AuthUiState()
    data object Loading: AuthUiState()
    data class Error(val errorMessage: String): AuthUiState()
}