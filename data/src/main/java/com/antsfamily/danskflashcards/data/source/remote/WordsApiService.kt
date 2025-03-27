package com.antsfamily.danskflashcards.data.source.remote

import com.antsfamily.danskflashcards.data.model.WordApiModel
import retrofit2.http.GET

interface WordsApiService {

    companion object {
        const val API_ENDPOINT =
            "https://dansk-flashcards-default-rtdb.europe-west1.firebasedatabase.app"
    }

    @GET("/words.json")
    suspend fun getWords(): List<WordApiModel>
}
