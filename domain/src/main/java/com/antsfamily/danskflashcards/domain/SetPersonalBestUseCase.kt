package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.repository.DataRepository
import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_NAME
import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_SCORE
import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_TIMESTAMP
import com.antsfamily.danskflashcards.domain.model.UserWithPersonalBestDomain
import com.google.firebase.firestore.FieldValue
import javax.inject.Inject

class SetPersonalBestUseCase @Inject constructor(
    private val repository: DataRepository,
) {
    suspend operator fun invoke(params: UserWithPersonalBestDomain) {
        val userData = hashMapOf(
            FIELD_NAME to params.name,
            FIELD_SCORE to params.score,
            FIELD_TIMESTAMP to FieldValue.serverTimestamp()
        )
        return repository.updateUser(params.id, userData)
    }

    suspend operator fun invoke(id: String, name: String, score: Int) {
        val userData = hashMapOf(
            FIELD_NAME to name,
            FIELD_SCORE to score,
            FIELD_TIMESTAMP to FieldValue.serverTimestamp()
        )
        return repository.updateUser(id, userData)
    }
}