package com.antsfamily.danskflashcards.domain.model

import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_NAME
import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_SCORE
import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_TIMESTAMP
import com.antsfamily.danskflashcards.data.util.orZero
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot

data class UserDomain(
    val id: String,
    val username: String,
    val score: Int,
    val date: Timestamp?,
    val isCurrentUser: Boolean,
)

fun QuerySnapshot.mapToDomain(userId: String): List<UserDomain> {
    return this.mapNotNull {
        val data = it.data
        val id = it.reference.id as? String
        val username = data[FIELD_NAME] as? String
        val score = data[FIELD_SCORE] as? Long
        val date = data[FIELD_TIMESTAMP] as? Timestamp
        UserDomain(
            id = id.orEmpty(),
            username = username.orEmpty(),
            score = score.orZero().toInt(),
            date = date,
            isCurrentUser = userId == id
        )
    }
}