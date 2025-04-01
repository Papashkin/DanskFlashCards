package com.antsfamily.danskflashcards.ui.settings

import com.antsfamily.danskflashcards.core.model.ErrorType

sealed class SettingsUiState {
    data object Loading : SettingsUiState()
    data class Content(
        val username: String,
        val learningLanguage: String,
        val appVersion: String
    ) : SettingsUiState()
    data class Error(val type: ErrorType): SettingsUiState()
}