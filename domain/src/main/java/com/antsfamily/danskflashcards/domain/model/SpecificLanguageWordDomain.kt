package com.antsfamily.danskflashcards.domain.model

data class SpecificLanguageWordDomain(
    val id: Int,
    val type: LanguageType,
    val value: String,
)
