package com.antsfamily.danskflashcards.ui.home.model

import java.util.Date

data class UserModel(
    val id: String,
    val username: String,
    val isCurrentUser: Boolean,
    val score: Int,
    val date: Date?,
)
