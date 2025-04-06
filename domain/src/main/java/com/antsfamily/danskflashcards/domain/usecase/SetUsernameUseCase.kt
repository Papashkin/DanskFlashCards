package com.antsfamily.danskflashcards.domain.usecase

import com.antsfamily.danskflashcards.domain.repository.DataRepository
import javax.inject.Inject

class SetUsernameUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(id: String, name: String) {
        try {
            return repository.updateUserName(id, name)
        } catch (e: Exception) {
            throw e
        }
    }
}