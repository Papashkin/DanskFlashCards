package com.antsfamily.danskflashcards.data.model

import com.google.gson.annotations.SerializedName

data class WordApiModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("word_dk")
    val danish: String,
    @SerializedName("word_de")
    val german: String,
    @SerializedName("word_en")
    val english: String,
    @SerializedName("word_ru")
    val russian: String
)
