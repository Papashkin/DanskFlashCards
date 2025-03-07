package com.antsfamily.danskflashcards.ui.game.model

data class GameOverModel(
    val newResult: Int = 0,
    val bestResult: Int = 0,
) {

    val isNewRecord: Boolean
        get() = newResult > bestResult
}
