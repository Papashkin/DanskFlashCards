package com.antsfamily.danskflashcards.data.model

import com.google.firebase.Timestamp

data class UserApiModel(
    val id: String,
    val username: String,
    val score: Int,
    val date: Timestamp?,
    val isCurrentUser: Boolean,
)