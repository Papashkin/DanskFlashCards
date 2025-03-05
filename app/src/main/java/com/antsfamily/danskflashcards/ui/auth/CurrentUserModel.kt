package com.antsfamily.danskflashcards.ui.auth

import android.os.Parcelable
import com.antsfamily.danskflashcards.ui.home.model.UserModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentUserModel(
    val userId: String,
    val username: String,
    val email: String,
) : Parcelable {

    fun isValid(): Boolean = username.isNotBlank() && this.email.isNotBlank()

    fun mapToUserModel(): UserModel {
        return UserModel(id = userId, username = username, true, 0, null)
    }
}