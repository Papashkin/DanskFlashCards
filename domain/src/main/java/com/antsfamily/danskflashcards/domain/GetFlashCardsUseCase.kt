package com.antsfamily.danskflashcards.domain

import android.content.Context
import com.antsfamily.danskflashcards.data.model.WordApiModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.InputStreamReader
import javax.inject.Inject
import kotlin.coroutines.resume

class GetFlashCardsUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    companion object {
        private const val FLASHCARDS_ASSET_NAME = "dansk_flashcards.json"
    }

    private var cards: List<WordApiModel>? = null

    suspend fun run(): List<WordApiModel> = cards ?: run { getFlashCardsRemote() }

    private suspend fun getFlashCardsRemote(): List<WordApiModel> = suspendCancellableCoroutine {
        val cards = try {
            val inputStream = context.assets.open(FLASHCARDS_ASSET_NAME)
            val reader = InputStreamReader(inputStream)
            val listType = object : TypeToken<List<WordApiModel>>() {}.type
            val flashCards = gson.fromJson<List<WordApiModel?>>(reader, listType)
            cards = flashCards.mapNotNull { item -> item }
            flashCards
        } catch (e: Exception) {
            emptyList<WordApiModel>()
        }
        it.resume(cards.mapNotNull { item -> item })
    }
}