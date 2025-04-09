package com.antsfamily.danskflashcards.core.model

import com.antsfamily.danskflashcards.core.util.DOT
import com.antsfamily.danskflashcards.core.util.SEPARATOR_SPACE
import com.antsfamily.danskflashcards.domain.model.UserDomain

data class UserItem(
    val id: String,
    val index: Int?,
    val username: String,
    val avatar: Avatar,
    val isCurrentUser: Boolean,
    val score: Int,
) {

    val place: Int?
        get() = index?.plus(1)

    fun isFirstTime(): Boolean = (score == 0)
}

fun UserDomain.toItem(index: Int): UserItem {
    val name = this.username.trim().split(SEPARATOR_SPACE)
    val username = if (name.size <= 1) {
        name.first()
    } else {
        listOf(name.first(), name.last().take(1).plus(DOT))
            .joinToString(separator = SEPARATOR_SPACE)
    }
    return UserItem(
        id = id,
        index = index,
        username = username,
        score = score,
        isCurrentUser = isCurrentUser,
        avatar = avatarId?.let { Avatar.entries.toTypedArray()[it] } ?: Avatar.DEFAULT,
    )
}