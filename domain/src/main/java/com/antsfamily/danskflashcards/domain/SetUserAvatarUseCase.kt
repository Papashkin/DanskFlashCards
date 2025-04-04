package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.repository.DataRepository
import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_AVATAR_ID
import javax.inject.Inject

class SetUserAvatarUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(id: String, avatarId: Int) {
        try {
            val userData = hashMapOf<String, Any>(FIELD_AVATAR_ID to avatarId)
            return repository.updateUser(id, userData)
        } catch (e: Exception) {
            throw e
        }
    }
}