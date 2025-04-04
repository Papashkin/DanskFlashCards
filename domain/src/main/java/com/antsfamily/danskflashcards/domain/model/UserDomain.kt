package com.antsfamily.danskflashcards.domain.model

import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_AVATAR_ID
import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_NAME
import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_SCORE
import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_TIMESTAMP
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

data class UserDomain(
    val id: String,
    val username: String,
    val score: Int,
    val date: Timestamp?,
    val isCurrentUser: Boolean,
    val avatarId: Int?
)

fun QuerySnapshot.mapToDomain(userId: String): List<UserDomain> {
    return this.mapNotNull {
        val data = it.data
        val id = it.reference.id as? String
        val username = data[FIELD_NAME] as? String
        val score = data[FIELD_SCORE] as? Long
        val date = data[FIELD_TIMESTAMP] as? Timestamp
        val avatarId = it[FIELD_AVATAR_ID] as? Long
        UserDomain(
            id = id.orEmpty(),
            username = username.orEmpty(),
            score = score?.toInt() ?: 0,
            date = date,
            isCurrentUser = userId == id,
            avatarId = avatarId?.toInt()
        )
    }
}

fun DocumentSnapshot?.mapToDomain(userId: String): UserDomain? {
    return this?.data?.let {
        val id = this.reference.id as? String
        val username = it[FIELD_NAME] as? String
        val score = it[FIELD_SCORE] as? Long
        val date = it[FIELD_TIMESTAMP] as? Timestamp
        val avatarId = it[FIELD_AVATAR_ID] as? Long
        UserDomain(
            id = id.orEmpty(),
            username = username.orEmpty(),
            score = score?.toInt() ?: 0,
            date = date,
            isCurrentUser = userId == id,
            avatarId = avatarId?.toInt()
        )
    }
}