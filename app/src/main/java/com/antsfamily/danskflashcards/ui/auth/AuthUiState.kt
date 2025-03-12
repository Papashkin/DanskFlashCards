package com.antsfamily.danskflashcards.ui.auth

import com.antsfamily.danskflashcards.core.model.ErrorType

sealed class AuthUiState {
    data object Default: AuthUiState()
    data object Loading: AuthUiState()
    data class Error(val type: ErrorType): AuthUiState()
}