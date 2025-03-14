package com.antsfamily.danskflashcards.domain.model

data class UserWithPersonalBestDomain(
    val id: String,
    val name: String,
    val score: Int,
)