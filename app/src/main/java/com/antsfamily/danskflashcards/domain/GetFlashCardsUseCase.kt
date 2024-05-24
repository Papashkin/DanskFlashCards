package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.WordApiModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GetFlashCardsUseCase @Inject constructor(
    private val firebaseDatabase: DatabaseReference,
    private val gson: Gson
) : BaseUseCase<Unit, List<WordApiModel>>() {

    private var cards: List<WordApiModel>? = null

    override suspend fun run(params: Unit): List<WordApiModel> {
        return cards ?: run { getFlashCardsRemote() }
    }

    private suspend fun getFlashCardsRemote(): List<WordApiModel> = suspendCancellableCoroutine {
        try {
            firebaseDatabase.get().addOnSuccessListener { snapshot ->
                val cards = if (snapshot.exists()) {
                    val snapshotValue =
                        snapshot.getValue<ArrayList<HashMap<String, Any>>>().orEmpty()
                    val jsonData = gson.toJson(snapshotValue)
                    val listType = object : TypeToken<List<WordApiModel>>() {}.type
                    val flashCards = gson.fromJson<List<WordApiModel?>>(jsonData, listType)
                    cards = flashCards.mapNotNull { item -> item }
                    flashCards
                } else {
                    emptyList()
                }
                it.resume(cards.mapNotNull { item -> item })
            }.addOnFailureListener { exception ->
                it.resumeWithException(exception)
            }
        } catch (e: Exception) {
            it.resumeWithException(e)
        }
    }
}