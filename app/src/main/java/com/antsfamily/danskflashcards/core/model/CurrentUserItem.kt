package com.antsfamily.danskflashcards.core.model

import com.antsfamily.danskflashcards.core.util.SEPARATOR_SPACE
import com.antsfamily.danskflashcards.core.util.DOT
import com.antsfamily.danskflashcards.domain.model.UserDomain

data class CurrentUserItem(
    val userId: String,
    val username: String,
) {

    fun isValid(): Boolean = username.isNotBlank() && userId.isNotBlank()

    fun mapToUserItem(): UserItem {
        val name = this.username.trim().split(SEPARATOR_SPACE)
        val username = if (name.size <= 1) {
            name.first()
        } else {
            listOf(name.first(), name.last().take(1).plus(DOT))
                .joinToString(separator = SEPARATOR_SPACE)
        }
        return UserItem(
            id = userId,
            username = username,
            isCurrentUser = true,
            score = 0,
            avatar = Avatar.DEFAULT,
            index = null
        )
    }
}

fun UserDomain.mapToItem(): CurrentUserItem {
    return CurrentUserItem(userId = id, username = username)
}