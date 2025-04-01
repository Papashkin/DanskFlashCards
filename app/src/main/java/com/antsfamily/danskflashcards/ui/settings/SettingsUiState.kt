package com.antsfamily.danskflashcards.ui.settings

import com.antsfamily.danskflashcards.core.model.ErrorType
import com.antsfamily.danskflashcards.domain.model.LanguageType

sealed class SettingsUiState {
    data object Loading : SettingsUiState()
    data class Content(
        val username: String,
        val learningLanguage: LanguageType,
        val appVersion: String
    ) : SettingsUiState()
    data class Error(val type: ErrorType): SettingsUiState()
}