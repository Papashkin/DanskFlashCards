package com.antsfamily.danskflashcards.data.util

import com.antsfamily.danskflashcards.data.model.UserApiModel
import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_NAME
import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_SCORE
import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_TIMESTAMP
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot

fun QuerySnapshot.mapToUserApiModels(userId: String): List<UserApiModel> {
    return this.mapNotNull {
        val data = it.data
        val id = it.reference.id as? String
        val username = data[FIELD_NAME] as? String
        val score = data[FIELD_SCORE] as? Long
        val date = data[FIELD_TIMESTAMP] as? Timestamp
        UserApiModel(
            id = id.orEmpty(),
            username = username.orEmpty(),
            score = score.orZero().toInt(),
            date = date,
            isCurrentUser = userId == id
        )
    }
}