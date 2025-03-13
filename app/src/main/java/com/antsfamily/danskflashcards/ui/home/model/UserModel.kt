package com.antsfamily.danskflashcards.ui.home.model

import com.antsfamily.danskflashcards.data.model.UserApiModel
import com.antsfamily.danskflashcards.core.util.toIsoString

data class UserModel(
    val id: String,
    val name: String,
    val surname: String,
    val isCurrentUser: Boolean,
    val score: Int,
    val date: String?,
) {

    fun isFirstTime(): Boolean = date.isNullOrBlank()
}

fun UserApiModel.toModel(currentUserId: String): UserModel {
    val username = this.username.trim().split(SEPARATOR_SPACE)
    return UserModel(
        id = id,
        name = username.first(),
        surname = username.last(),
        score = score,
        date = date?.toIsoString(),
        isCurrentUser = id == currentUserId
    )
}