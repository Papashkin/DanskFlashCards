package com.antsfamily.danskflashcards.data.model

import com.antsfamily.danskflashcards.ui.home.model.UserModel
import com.antsfamily.danskflashcards.util.toIsoString
import com.google.firebase.Timestamp

data class UserApiModel(
    val id: String,
    val username: String,
    val score: Int,
    val date: Timestamp?,
    val isCurrentUser: Boolean,
) {

    fun toModel(currentUserId: String): UserModel {
        return UserModel(
            id = id,
            username = username,
            score = score,
            date = date?.toIsoString(),
            isCurrentUser = id == currentUserId
        )
    }
}