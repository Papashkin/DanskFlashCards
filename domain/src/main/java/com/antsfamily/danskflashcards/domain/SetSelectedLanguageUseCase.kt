package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.repository.DataRepository
import com.antsfamily.danskflashcards.domain.model.LanguageType
import javax.inject.Inject

class SetSelectedLanguageUseCase @Inject constructor(
    private val repository: DataRepository
) {

    suspend operator fun invoke(type: LanguageType) {
        return repository.setSelectedLanguage(type.name)
    }
}