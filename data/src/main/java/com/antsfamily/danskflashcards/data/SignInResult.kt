package com.antsfamily.danskflashcards.data

import android.os.Parcelable
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
) : Parcelable