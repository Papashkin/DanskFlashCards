package com.antsfamily.danskflashcards.data

import com.google.gson.annotations.SerializedName

data class Words(val words: List<Word>)

data class Word(
    @SerializedName("word_da")
    val danish: String,
    @SerializedName("word_en")
    val english: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("word_ru")
    val russian: String
)
