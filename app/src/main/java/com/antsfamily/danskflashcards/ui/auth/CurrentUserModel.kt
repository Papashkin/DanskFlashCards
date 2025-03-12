package com.antsfamily.danskflashcards.ui.auth

import com.antsfamily.danskflashcards.data.CurrentUserApiModel
import com.antsfamily.danskflashcards.ui.home.model.UserModel

data class CurrentUserModel(
    val userId: String,
    val username: String,
    val email: String,
) {

    fun isValid(): Boolean = username.isNotBlank() && this.email.isNotBlank()

    fun mapToUserModel(): UserModel {
        val username = username.split(" ")
        return UserModel(
            id = userId,
            name = username.first(),
            surname = username.last(),
            isCurrentUser = true,
            score = 0,
            date = null
        )
    }
}

fun CurrentUserApiModel.mapToModel(): CurrentUserModel {
    return CurrentUserModel(userId = userId, username = username, email = email)
}