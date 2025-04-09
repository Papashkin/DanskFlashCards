package com.antsfamily.danskflashcards.ui.home.model

import com.antsfamily.danskflashcards.core.model.Avatar
import com.antsfamily.danskflashcards.domain.model.UserDomain

const val DOT = "."
const val SEPARATOR_SPACE = " "

data class LeaderItem(
    val index: Int,
    val username: String,
    val avatar: Avatar,
    val isUser: Boolean,
    val score: Int,
) {
    val place: Int
        get() = (index + 1)
}

//TODO rethink UserItem and LeaderItem
fun UserDomain.toLeaderItem(index: Int): LeaderItem {
    val name = this.username.trim().split(SEPARATOR_SPACE)
    val username = if (name.size <= 1) {
        name.first()
    } else {
        listOf(name.first(), name.last().take(1).plus(DOT))
            .joinToString(separator = SEPARATOR_SPACE)
    }
    return LeaderItem(
        index = index,
        username = username,
        isUser = isCurrentUser,
        score = score,
        avatar = avatarId?.let { Avatar.entries.toTypedArray()[it] } ?: Avatar.DEFAULT
    )
}

fun UserItem.toLeaderItem(index: Int): LeaderItem {
    val username = surname?.let {
        listOf(name, surname.take(1).plus(DOT)).joinToString(separator = SEPARATOR_SPACE)
    } ?: run {
        name
    }

    return LeaderItem(
        index = index,
        username = username,
        isUser = isCurrentUser,
        score = score,
        avatar = avatar
    )
}