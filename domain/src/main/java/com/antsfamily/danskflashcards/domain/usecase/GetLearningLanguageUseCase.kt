package com.antsfamily.danskflashcards.domain.usecase

import com.antsfamily.danskflashcards.domain.repository.DataRepository
import com.antsfamily.danskflashcards.domain.model.LanguageType
import javax.inject.Inject

class GetLearningLanguageUseCase @Inject constructor(
    private val repository: DataRepository
) {
    suspend operator fun invoke(): LanguageType {
        val languageName = repository.getLearningLanguage()
        return languageName?.let {
            LanguageType.valueOf(languageName)
        } ?: LanguageType.EN
    }
}