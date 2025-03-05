package com.antsfamily.danskflashcards.data.model

import com.antsfamily.danskflashcards.ui.home.model.UserModel
import java.util.Date

data class UserApiModel(
    val id: String,
    val username: String,
    val score: Int,
    val date: Date?,
    val isCurrentUser: Boolean,
) {

    fun toModel(currentUserId: String): UserModel {
        return UserModel(
            id = id,
            username = username,
            score = score,
            date = date,
            isCurrentUser = id == currentUserId
        )
    }
}