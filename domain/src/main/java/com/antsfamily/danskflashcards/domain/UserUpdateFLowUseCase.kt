package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.model.UserApiModel
import com.antsfamily.danskflashcards.data.repository.FirestoreDataRepository
import com.antsfamily.danskflashcards.data.util.mapToUserApiModels
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserUpdateFLowUseCase @Inject constructor(
    private val repository: FirestoreDataRepository
) {

    suspend fun run(params: String): Flow<List<UserApiModel>> {
        return repository.getUsersFLow().map {
            it.mapToUserApiModels(userId = params)
        }
    }
}
