package com.antsfamily.danskflashcards.data

import android.util.Log
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
) {

    companion object {
        fun Word?.mapToModel(isDanish: Boolean): WordModel {
            if ((this == null) || (this.id == null) || (this.danish == null) || (this.english == null)) {
                Log.wtf(Word::class.java.simpleName, "Break!")
            }
            return WordModel(
                id = this!!.id,
                value = if (isDanish) this.danish else this.english,
                guessed = false,
            )
        }
    }
}
