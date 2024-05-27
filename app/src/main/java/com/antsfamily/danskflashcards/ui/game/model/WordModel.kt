package com.antsfamily.danskflashcards.ui.game.model

data class WordModel(
    val id: Int,
    val value: String,
    val isSelected: Boolean,
    val isGuessed: Boolean,
    val isWrong: Boolean,
)


val WORD_CARDS_DANISH = listOf(
    WordModel(
        value = "kolonne",
        id = 986,
        isGuessed = false,
        isSelected = false,
        isWrong = false
    ),
    WordModel(
        value = "molekyle",
        id = 987,
        isGuessed = false,
        isSelected = true,
        isWrong = false
    ),
    WordModel(
        value = "vælg",
        id = 988,
        isGuessed = true,
        isSelected = true,
        isWrong = false
    ),
    WordModel(
        value = "forkert",
        id = 989,
        isGuessed = false,
        isSelected = false,
        isWrong = false
    ),
    WordModel(
        value = "grå",
        id = 990,
        isGuessed = false,
        isSelected = false,
        isWrong = true
    )
)
val WORD_CARDS_ENGLISH = listOf(
    WordModel(
        value = "column",
        id = 991,
        isGuessed = false,
        isSelected = true,
        isWrong = false
    ),
    WordModel(
        value = "molecule",
        id = 992,
        isGuessed = true,
        isSelected = false,
        isWrong = false
    ),
    WordModel(
        value = "select",
        id = 993,
        isGuessed = false,
        isSelected = false,
        isWrong = false
    ),
    WordModel(
        value = "wrong",
        id = 994,
        isGuessed = false,
        isSelected = true,
        isWrong = false
    ),
    WordModel(
        value = "gray",
        id = 996,
        isGuessed = false,
        isSelected = false,
        isWrong = false
    ),
)