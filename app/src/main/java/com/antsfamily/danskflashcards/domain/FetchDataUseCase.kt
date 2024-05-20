package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.Word
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FetchDataUseCase @Inject constructor(
    private val gson: Gson
) : BaseUseCase<Unit, List<Word?>>() {

    override suspend fun run(params: Unit): List<Word?> = suspendCancellableCoroutine {
        try {
            getDatabase().get().addOnSuccessListener { snapshot ->
                val words = if (snapshot.exists()) {
                    val snapshotValue =
                        snapshot.getValue<ArrayList<HashMap<String, Any>>>().orEmpty()
                    val jsonData = gson.toJson(snapshotValue)
                    val listType = object : TypeToken<List<Word>>() {}.type
                    gson.fromJson<List<Word?>>(jsonData, listType)
                } else {
                    emptyList()
                }
                it.resume(words)
            }.addOnFailureListener { exception ->
                it.resumeWithException(exception)
            }
        } catch (e: Exception) {
            it.resumeWithException(e)
        }
    }

    private fun getDatabase(): DatabaseReference = FirebaseDatabase.getInstance().reference
}