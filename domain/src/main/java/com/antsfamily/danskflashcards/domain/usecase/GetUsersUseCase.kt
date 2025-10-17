package com.antsfamily.danskflashcards.domain.usecase

import com.antsfamily.danskflashcards.domain.model.UserDomain
import com.antsfamily.danskflashcards.domain.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: DataRepository,
) {
    suspend operator fun invoke(userId: String): List<UserDomain> = withContext(Dispatchers.IO) {
        val users = repository.getUsers()
        users.map { if (it.id == userId) it.copy(isCurrentUser = true) else it }
    }
}