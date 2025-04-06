package com.antsfamily.danskflashcards.data.model

import com.antsfamily.danskflashcards.domain.model.WordDomain
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

fun List<WordApiModel>.mapToDomain(): List<WordDomain> {
    return this.map { word ->
        WordDomain(
            id = word.id,
            danish = word.danish,
            german = word.german,
            english = word.english,
            russian = word.russian,
        )
    }
}
