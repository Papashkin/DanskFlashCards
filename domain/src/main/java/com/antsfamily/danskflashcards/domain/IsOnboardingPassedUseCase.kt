package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.repository.DataRepository
import javax.inject.Inject

class IsOnboardingPassedUseCase @Inject constructor(
    private val repository: DataRepository
) {

    suspend operator fun invoke(): Boolean {
        return repository.isOnboardingPassed()
    }
}