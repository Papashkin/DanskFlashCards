package com.antsfamily.danskflashcards.domain.usecase

import com.antsfamily.danskflashcards.domain.repository.DataRepository
import javax.inject.Inject

class SetUserAvatarUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(id: String, avatarId: Int) {
        try {
            return repository.updateUserAvatar(id, avatarId)
        } catch (e: Exception) {
            throw e
        }
    }
}