package com.antsfamily.danskflashcards.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

@Parcelize
data class UserData(
    val userId: String,
    val username: String,
    val email: String,
) : Parcelable