package com.antsfamily.danskflashcards.domain.usecase

import com.antsfamily.danskflashcards.domain.repository.DataRepository
import com.antsfamily.danskflashcards.domain.model.UserDomain
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: DataRepository,
) {
    suspend operator fun invoke(userId: String): List<UserDomain> {
        try {
            val users = repository.getUsers()
            return users.map { if (it.id == userId) it.copy(isCurrentUser = true) else it }
        } catch (e: Exception) {
            throw e
        }
    }
}