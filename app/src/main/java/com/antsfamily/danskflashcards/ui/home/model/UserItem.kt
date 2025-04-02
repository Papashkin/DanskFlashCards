package com.antsfamily.danskflashcards.ui.home.model

import com.antsfamily.danskflashcards.domain.model.UserDomain
import com.antsfamily.danskflashcards.core.util.toIsoString

data class UserItem(
    val id: String,
    val name: String,
    val surname: String,
    val isCurrentUser: Boolean,
    val score: Int,
    val date: String?,
) {

    fun isFirstTime(): Boolean = (score == 0) && date.isNullOrBlank()
}

fun UserDomain.toItem(currentUserId: String): UserItem {
    val username = this.username.trim().split(SEPARATOR_SPACE)
    return UserItem(
        id = id,
        name = username.first(),
        surname = username.last(),
        score = score,
        date = date?.toIsoString(),
        isCurrentUser = id == currentUserId
    )
}