package com.antsfamily.danskflashcards.domain.usecase

import com.antsfamily.danskflashcards.domain.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class IsOnboardingPassedUseCase @Inject constructor(
    private val repository: DataRepository
) {

    suspend operator fun invoke(): Boolean = withContext(Dispatchers.IO) {
        repository.isOnboardingPassed()
    }
}