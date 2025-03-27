package com.antsfamily.danskflashcards.data.source.local

interface LocalSource {
    suspend fun getWebClientId(): String?
    fun getAppVersion(): String?
}