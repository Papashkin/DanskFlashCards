package com.antsfamily.danskflashcards.ui.onboarding2

import com.antsfamily.danskflashcards.ui.onboarding.model.LanguageItem

sealed class Onboarding2UiState {
    data object Loading : Onboarding2UiState()
    data class Content(
        val languages: List<LanguageItem>,
        val isButtonAvailable: Boolean,
        val isButtonLoadingVisible: Boolean
    ) : Onboarding2UiState()
}