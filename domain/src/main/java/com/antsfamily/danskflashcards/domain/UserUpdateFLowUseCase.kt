package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.repository.DataRepository
import com.antsfamily.danskflashcards.domain.model.UserDomain
import com.antsfamily.danskflashcards.domain.model.mapToDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserUpdateFLowUseCase @Inject constructor(
    private val repository: DataRepository
) {

    suspend operator fun invoke(params: String): Flow<List<UserDomain>> {
        try {
            return repository.getUsersFLow().map {
                it.mapToDomain(userId = params)
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
