package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.repository.DataRepository
import javax.inject.Inject

class GetWordsAmountUseCase @Inject constructor(
    private val repository: DataRepository
) {

    suspend operator fun invoke(): Int {
        try {
            val response = repository.getWords()
            return response.size
        } catch (e: Exception) {
            throw e
        }
    }
}