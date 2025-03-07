package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.model.UserWithPersonalBestApiModel
import com.antsfamily.danskflashcards.data.repository.FirestoreDataRepository
import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_NAME
import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_SCORE
import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_TIMESTAMP
import com.google.firebase.firestore.FieldValue
import javax.inject.Inject

class SetPersonalBestUseCase @Inject constructor(
    private val repository: FirestoreDataRepository,
) {
    suspend fun run(params: UserWithPersonalBestApiModel) {
        val userData = hashMapOf(
            FIELD_NAME to params.name,
            FIELD_SCORE to params.score,
            FIELD_TIMESTAMP to FieldValue.serverTimestamp()
        )
        return repository.updateUser(params.id, userData)
    }
}