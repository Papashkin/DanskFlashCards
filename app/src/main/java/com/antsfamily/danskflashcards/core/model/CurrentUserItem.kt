package com.antsfamily.danskflashcards.core.model

import com.antsfamily.danskflashcards.domain.model.UserDomain
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
            surname = if (username.size > 1) username.last() else null,
            isCurrentUser = true,
            score = 0,
            avatar = Avatar.DEFAULT
        )
    }
}

fun UserDomain.mapToItem(): CurrentUserItem {
    return CurrentUserItem(userId = id, username = username)
}