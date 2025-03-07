package com.antsfamily.danskflashcards.ui.home.model

import com.antsfamily.danskflashcards.data.model.UserApiModel
import com.antsfamily.danskflashcards.core.util.toIsoString

data class UserModel(
    val id: String,
    val username: String,
    val isCurrentUser: Boolean,
    val score: Int,
    val date: String?,
) {

    fun isFirstTime(): Boolean = date.isNullOrBlank()
}

fun UserApiModel.toModel(currentUserId: String): UserModel {
    return UserModel(
        id = id,
        username = username,
        score = score,
        date = date?.toIsoString(),
        isCurrentUser = id == currentUserId
    )
}