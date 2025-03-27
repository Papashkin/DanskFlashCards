package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.repository.DataRepository
import com.antsfamily.danskflashcards.domain.model.LanguageType
import com.antsfamily.danskflashcards.domain.model.WordDomain
import com.antsfamily.danskflashcards.domain.model.mapToDomain
import javax.inject.Inject

class GetWordsUseCase @Inject constructor(
    private val repository: DataRepository
) {

    suspend operator fun invoke(
        firstLanguage: LanguageType,
        secondLanguage: LanguageType
    ): Pair<List<WordDomain>, List<WordDomain>> {
        try {
            val words = repository.getWords()

            val wordsFirstLang = words.mapNotNull { it.mapToDomain(firstLanguage) }
            val wordsSecondLang = words.mapNotNull { it.mapToDomain(secondLanguage) }

            return wordsFirstLang to wordsSecondLang
        } catch (e: Exception) {
            throw e
        }
    }
}