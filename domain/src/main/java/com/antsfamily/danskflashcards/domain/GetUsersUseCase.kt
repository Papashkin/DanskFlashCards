package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.repository.DataRepository
import com.antsfamily.danskflashcards.domain.model.UserDomain
import com.antsfamily.danskflashcards.domain.model.mapToDomain
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: DataRepository,
) {
    suspend operator fun invoke(params: String): List<UserDomain> {
        try {
            val data = repository.getUsers()
            return data.mapToDomain(params)
        } catch (e: Exception) {
            throw e
        }
    }
}