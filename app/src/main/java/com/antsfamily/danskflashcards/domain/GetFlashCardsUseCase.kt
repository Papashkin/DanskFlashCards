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
) : BaseUseCase<Unit, List<WordApiModel>>() {

    private var cards: List<WordApiModel>? = null

    override suspend fun run(params: Unit): List<WordApiModel> {
        return cards ?: run { getFlashCardsRemote() }
    }

    private suspend fun getFlashCardsRemote(): List<WordApiModel> = suspendCancellableCoroutine {
        val cards2 = try {
            val inputStream = context.assets.open("dansk_flashcards.json")
            val reader = InputStreamReader(inputStream)
            val listType = object : TypeToken<List<WordApiModel>>() {}.type
            val flashCards = gson.fromJson<List<WordApiModel?>>(reader, listType)
            cards = flashCards.mapNotNull { item -> item }
            flashCards
        } catch (e: Exception) {
            emptyList<WordApiModel>()
        }
        it.resume(cards2.mapNotNull { item -> item })
    }
}