package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.ui.game.model.UserWithPersonalBestModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SetPersonalBestUseCase @Inject constructor(
    private val firestore: FirebaseFirestore
) : BaseUseCase<UserWithPersonalBestModel, Unit>() {

    override suspend fun run(params: UserWithPersonalBestModel) = suspendCancellableCoroutine {
        val userData = hashMapOf(
            "name" to params.name,
            "score" to params.score,
            "timestamp" to FieldValue.serverTimestamp()
        )
        firestore
            .collection("users")
            .document(params.id)
            .set(userData)
            .addOnSuccessListener { _ ->
                it.resume(Unit)
            }
            .addOnFailureListener { exception ->
                it.resumeWithException(exception)
            }
    }
}