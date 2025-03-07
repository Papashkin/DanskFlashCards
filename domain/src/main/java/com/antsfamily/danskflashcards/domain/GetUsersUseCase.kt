package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.model.UserApiModel
import com.antsfamily.danskflashcards.data.repository.FirestoreDataRepository
import com.antsfamily.danskflashcards.data.util.mapToUserApiModels
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: FirestoreDataRepository,
) {
    suspend fun run(params: String): List<UserApiModel> {
        val data = repository.getUsers()
        return data.mapToUserApiModels(params)
    }
}