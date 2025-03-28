package com.antsfamily.danskflashcards.ui.onboarding

import com.antsfamily.danskflashcards.ui.onboarding.model.LanguageItem

sealed class OnboardingUiState {
    data object Loading : OnboardingUiState()
    data class Content(
        val languages: List<LanguageItem>,
        val isButtonAvailable: Boolean,
        val isButtonLoadingVisible: Boolean
    ) : OnboardingUiState()
}