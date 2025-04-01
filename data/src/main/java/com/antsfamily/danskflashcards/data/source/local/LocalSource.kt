package com.antsfamily.danskflashcards.data.source.local

interface LocalSource {
    suspend fun getWebClientId(): String?
    fun getAppVersion(): String?

    suspend fun isOnboardingPassed(): Boolean
    suspend fun getLearningLanguage(): String?
    suspend fun setLearningLanguage(language: String)
    suspend fun getPrimaryLanguage(): String?
    suspend fun setPrimaryLanguage(language: String)
    suspend fun setOnboardingPassed()
}