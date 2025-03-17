package com.antsfamily.danskflashcards.data.source.local

import com.antsfamily.danskflashcards.data.model.WordApiModel

interface LocalSource {
    fun getCards(): List<WordApiModel>
    fun getCardsAmount(): Int
    suspend fun getWebClientId(): String?
}