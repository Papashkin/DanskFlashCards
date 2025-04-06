package com.antsfamily.danskflashcards.domain.usecase

import com.antsfamily.danskflashcards.domain.repository.DataRepository
import javax.inject.Inject

class IsOnboardingPassedUseCase @Inject constructor(
    private val repository: DataRepository
) {

    suspend operator fun invoke(): Boolean {
        return repository.isOnboardingPassed()
    }
}