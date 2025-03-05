package com.antsfamily.danskflashcards.data

import android.os.Parcelable
import com.antsfamily.danskflashcards.ui.auth.CurrentUserModel
import kotlinx.parcelize.Parcelize

data class SignInResult(
    val data: CurrentUserApiModel?,
    val errorMessage: String?
)

@Parcelize
data class CurrentUserApiModel(
    val userId: String,
    val username: String,
    val email: String,
) : Parcelable {

    fun toModel(): CurrentUserModel {
        return CurrentUserModel(userId = userId, username = username, email = email)
    }
}