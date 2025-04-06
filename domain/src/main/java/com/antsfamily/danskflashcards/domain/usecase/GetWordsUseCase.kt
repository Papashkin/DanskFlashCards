package com.antsfamily.danskflashcards.domain.usecase

import com.antsfamily.danskflashcards.domain.repository.DataRepository
import com.antsfamily.danskflashcards.domain.model.SpecificLanguageWordDomain
import com.antsfamily.danskflashcards.domain.model.toSpecificLanguage
import javax.inject.Inject

class GetWordsUseCase @Inject constructor(
    private val getLearningLanguageUseCase: GetLearningLanguageUseCase,
    private val getPrimaryLanguageUseCase: GetPrimaryLanguageUseCase,
    private val repository: DataRepository
) {

    suspend operator fun invoke(): Pair<List<SpecificLanguageWordDomain>, List<SpecificLanguageWordDomain>> {
        try {
            val learningLanguage = getLearningLanguageUseCase()
            val primaryLanguage = getPrimaryLanguageUseCase()
            val allWords = repository.getWords()

            val learningWords = allWords.mapNotNull { it.toSpecificLanguage(learningLanguage) }
            val primaryWords = allWords.mapNotNull { it.toSpecificLanguage(primaryLanguage) }

            return learningWords to primaryWords
        } catch (e: Exception) {
            throw e
        }
    }
}