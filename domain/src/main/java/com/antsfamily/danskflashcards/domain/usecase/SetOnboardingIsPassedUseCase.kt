package com.antsfamily.danskflashcards.domain.usecase

import com.antsfamily.danskflashcards.domain.repository.DataRepository
import javax.inject.Inject

class SetOnboardingIsPassedUseCase @Inject constructor(
    private val repository: DataRepository
) {

    suspend operator fun invoke() {
        return repository.setOnboardingPassed()
    }
}