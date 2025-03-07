package com.antsfamily.danskflashcards.data.model

import com.google.gson.annotations.SerializedName

data class WordApiModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("word_da")
    val danish: String,
    @SerializedName("word_en")
    val english: String,
    @SerializedName("word_ru")
    val russian: String
)
