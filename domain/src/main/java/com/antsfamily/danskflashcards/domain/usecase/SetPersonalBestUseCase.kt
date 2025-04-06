package com.antsfamily.danskflashcards.domain.usecase

import com.antsfamily.danskflashcards.domain.repository.DataRepository
import javax.inject.Inject

class SetPersonalBestUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(id: String, name: String, score: Int) {
        try {
            return repository.updateUserScore(id, name, score)
        } catch (e: Exception) {
            throw e
        }
    }
}