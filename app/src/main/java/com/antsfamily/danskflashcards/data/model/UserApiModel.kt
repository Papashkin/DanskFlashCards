package com.antsfamily.danskflashcards.data.model

import java.util.Date

data class UserApiModel(
    val id: String,
    val username: String,
    val score: Int,
    val date: Date?
)