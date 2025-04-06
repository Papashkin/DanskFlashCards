package com.antsfamily.danskflashcards.domain.usecase

import com.antsfamily.danskflashcards.domain.repository.DataRepository
import com.antsfamily.danskflashcards.domain.model.UserDomain
import javax.inject.Inject

class GetLoggedInUserUseCase @Inject constructor(
    private val repository: DataRepository
) {

    suspend operator fun invoke(): UserDomain? {
        try {
            return repository.getCurrentUser()
        } catch (e: Exception) {
            throw e
        }
    }
}