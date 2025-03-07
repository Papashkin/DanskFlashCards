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
        return UserModel(id = userId, username = username, true, 0, null)
    }
}

fun CurrentUserApiModel.mapToModel(): CurrentUserModel {
    return CurrentUserModel(userId = userId, username = username, email = email)
}