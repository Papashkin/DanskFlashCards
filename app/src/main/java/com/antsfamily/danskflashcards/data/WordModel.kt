package com.antsfamily.danskflashcards.data

data class WordModel(
    val id: Int,
    val value: String,
    val isSelected: Boolean,
    val isGuessed: Boolean,
    val isWrong: Boolean,
)
