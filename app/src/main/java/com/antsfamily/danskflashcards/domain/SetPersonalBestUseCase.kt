package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.ui.game.model.UserWithPersonalBestModel
import com.antsfamily.danskflashcards.util.FirebaseConstants.COLLECTION_USERS
import com.antsfamily.danskflashcards.util.FirebaseConstants.FIELD_TIMESTAMP
import com.antsfamily.danskflashcards.util.FirebaseConstants.FIELD_NAME
import com.antsfamily.danskflashcards.util.FirebaseConstants.FIELD_SCORE
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
            FIELD_NAME to params.name,
            FIELD_SCORE to params.score,
            FIELD_TIMESTAMP to FieldValue.serverTimestamp()
        )
        firestore
            .collection(COLLECTION_USERS)
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