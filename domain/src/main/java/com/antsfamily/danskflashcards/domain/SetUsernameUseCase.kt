package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.repository.DataRepository
import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_NAME
import javax.inject.Inject

class SetUsernameUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(id: String, name: String) {
        try {
            val userData = hashMapOf<String, Any>(FIELD_NAME to name)
            return repository.updateUser(id, userData)
        } catch (e: Exception) {
            throw e
        }
    }
}