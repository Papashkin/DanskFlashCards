package com.antsfamily.danskflashcards.core.model

import com.antsfamily.danskflashcards.data.CurrentUserApiModel
import com.antsfamily.danskflashcards.ui.home.model.SEPARATOR_SPACE
import com.antsfamily.danskflashcards.ui.home.model.UserItem

data class CurrentUserItem(
    val userId: String,
    val username: String,
) {

    fun isValid(): Boolean = username.isNotBlank() && userId.isNotBlank()

    fun mapToUserItem(): UserItem {
        val username = username.split(SEPARATOR_SPACE)
        return UserItem(
            id = userId,
            name = username.first(),
            surname = username.last(),
            isCurrentUser = true,
            score = 0,
            date = null,
            avatar = Avatar.DEFAULT
        )
    }
}

fun CurrentUserApiModel.mapToItem(): CurrentUserItem {
    return CurrentUserItem(userId = userId, username = username)
}