package com.antsfamily.danskflashcards.domain.usecase

import com.antsfamily.danskflashcards.domain.repository.DataRepository
import com.antsfamily.danskflashcards.domain.model.UserDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserUpdateFLowUseCase @Inject constructor(
    private val repository: DataRepository
) {

    suspend operator fun invoke(userId: String): Flow<List<UserDomain>> {
        try {
            return repository.getUsersFLow().map { user ->
                user.map { if (it.id == userId) it.copy(isCurrentUser = true) else it }
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
