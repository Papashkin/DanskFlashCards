package com.antsfamily.danskflashcards.ui.home.model

import com.antsfamily.danskflashcards.core.model.Avatar
import com.antsfamily.danskflashcards.domain.model.UserDomain

data class UserItem(
    val id: String,
    val name: String,
    val surname: String,
    val isCurrentUser: Boolean,
    val score: Int,
    val avatar: Avatar,
) {

    fun isFirstTime(): Boolean = (score == 0)
}

fun UserDomain.toItem(currentUserId: String): UserItem {
    val username = this.username.trim().split(SEPARATOR_SPACE)
    return UserItem(
        id = id,
        name = username.first(),
        surname = username.last(),
        score = score,
        isCurrentUser = id == currentUserId,
        avatar = avatarId?.let { Avatar.entries.toTypedArray()[it] } ?: Avatar.DEFAULT
    )
}