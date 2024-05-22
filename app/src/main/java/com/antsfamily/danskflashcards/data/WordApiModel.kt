package com.antsfamily.danskflashcards.data

import com.google.gson.annotations.SerializedName

data class WordApiModel(
    @SerializedName("word_da")
    val danish: String,
    @SerializedName("word_en")
    val english: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("word_ru")
    val russian: String
)

fun WordApiModel?.mapToModel(isDanish: Boolean): WordModel? {
    if (this == null) return null

    return WordModel(
        id = this.id,
        value = if (isDanish) this.danish else this.english,
        isSelected = false,
        isGuessed = false,
        isWrong = false,
    )
}
