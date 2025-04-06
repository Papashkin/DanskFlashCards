package com.antsfamily.danskflashcards.domain.usecase

import com.antsfamily.danskflashcards.domain.repository.DataRepository
import com.antsfamily.danskflashcards.domain.model.LanguageType
import javax.inject.Inject

class SetLanguageUseCase @Inject constructor(
    private val repository: DataRepository
) {

    suspend operator fun invoke(type: LanguageType, isPrimary: Boolean) {
        return if (isPrimary) {
            repository.setPrimaryLanguage(type.name)
        } else {
            repository.setLearningLanguage(type.name)

        }
    }
}