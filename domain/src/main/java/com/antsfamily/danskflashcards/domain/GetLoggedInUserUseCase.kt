package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.repository.DataRepository
import com.antsfamily.danskflashcards.domain.model.UserDomain
import com.antsfamily.danskflashcards.domain.model.mapToDomain
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class GetLoggedInUserUseCase @Inject constructor(
    private val repository: DataRepository
) {

    suspend operator fun invoke(): UserDomain? {
        try {
            val user = repository.getCurrentUser()?.let { user ->
                repository.getUserByID(user.uid).mapToDomain(user.uid)
            }
            return user
        } catch (e: Exception) {
            throw e
        }
    }
}