package com.antsfamily.danskflashcards.ui.onboarding.model

import com.antsfamily.danskflashcards.domain.model.LanguageType

data class LanguageItem(
    val languageType: LanguageType,
    val isSelected: Boolean,
)