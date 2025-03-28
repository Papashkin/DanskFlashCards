package com.antsfamily.danskflashcards.data.source.local

interface LocalSource {
    suspend fun getWebClientId(): String?
    fun getAppVersion(): String?

    suspend fun isOnboardingPassed(): Boolean
    suspend fun getSelectedLanguage(): String?
    suspend fun setSelectedLanguage(language: String)
    suspend fun setOnboardingPassed()
}