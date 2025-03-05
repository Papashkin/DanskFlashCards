package com.antsfamily.danskflashcards.ui.home.model

data class UserModel(
    val id: String,
    val username: String,
    val isCurrentUser: Boolean,
    val score: Int,
    val date: String?,
)
