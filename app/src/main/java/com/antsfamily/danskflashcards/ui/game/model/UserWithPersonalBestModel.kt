package com.antsfamily.danskflashcards.ui.game.model

import com.antsfamily.danskflashcards.data.model.UserWithPersonalBestApiModel

data class UserWithPersonalBestModel(
    val id: String,
    val name: String,
    val score: Int,
)

fun UserWithPersonalBestModel.toApiModel(): UserWithPersonalBestApiModel {
    return UserWithPersonalBestApiModel(id = id, name = name, score = score)
}