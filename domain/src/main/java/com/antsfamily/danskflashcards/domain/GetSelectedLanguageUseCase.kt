package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.repository.DataRepository
import com.antsfamily.danskflashcards.domain.model.LanguageType
import javax.inject.Inject

class GetSelectedLanguageUseCase @Inject constructor(
    private val repository: DataRepository
) {
    suspend operator fun invoke(): LanguageType {
        val languageName = repository.getSelectedLanguage()
        return languageName?.let {
            LanguageType.valueOf(languageName)
        } ?: LanguageType.DK
    }
}