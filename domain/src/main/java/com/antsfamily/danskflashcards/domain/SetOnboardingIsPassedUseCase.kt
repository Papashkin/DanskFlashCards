package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.repository.DataRepository
import javax.inject.Inject

class SetOnboardingIsPassedUseCase @Inject constructor(
    private val repository: DataRepository
) {

    suspend operator fun invoke() {
        return repository.setOnboardingPassed()
    }
}