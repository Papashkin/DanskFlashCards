package com.antsfamily.danskflashcards.domain.model

data class UserDomain(
    val id: String,
    val username: String,
    val score: Int,
    val isCurrentUser: Boolean,
    val avatarId: Int?
)
