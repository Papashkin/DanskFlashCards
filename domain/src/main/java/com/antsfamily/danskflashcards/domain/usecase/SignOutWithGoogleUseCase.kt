package com.antsfamily.danskflashcards.domain.usecase

import com.antsfamily.danskflashcards.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke() {
        return repository.signOutFromGoogle()
    }
}
