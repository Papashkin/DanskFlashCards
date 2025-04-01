package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.repository.DataRepository
import com.antsfamily.danskflashcards.domain.model.LanguageType
import com.antsfamily.danskflashcards.domain.model.WordDomain
import com.antsfamily.danskflashcards.domain.model.mapToDomain
import javax.inject.Inject

class GetWordsUseCase @Inject constructor(
    private val getLearningLanguageUseCase: GetLearningLanguageUseCase,
    private val getPrimaryLanguageUseCase: GetPrimaryLanguageUseCase,
    private val repository: DataRepository
) {

    suspend operator fun invoke(): Pair<List<WordDomain>, List<WordDomain>> {
        try {
            val learningLanguage = getLearningLanguageUseCase()
            val primaryLanguage = getPrimaryLanguageUseCase()
            val words = repository.getWords()

            val wordsFirstLang = words.mapNotNull { it.mapToDomain(learningLanguage) }
            val wordsSecondLang = words.mapNotNull { it.mapToDomain(primaryLanguage) }

            return wordsFirstLang to wordsSecondLang
        } catch (e: Exception) {
            throw e
        }
    }
}