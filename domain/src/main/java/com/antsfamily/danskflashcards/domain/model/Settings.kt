package com.antsfamily.danskflashcards.domain.model

data class Settings(
    val userId: String,
    val username: String,
    val learningLanguage: LanguageType,
    val primaryLanguage: LanguageType,
    val avatarId: Int?,
    val appVersion: String
)