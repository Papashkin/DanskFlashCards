package com.antsfamily.danskflashcards.ui.onboarding

import com.antsfamily.danskflashcards.domain.model.LanguageType

sealed class OnboardingUiState {
    data object Loading: OnboardingUiState()
    data class Content(val languages: List<LanguageType>): OnboardingUiState()
}