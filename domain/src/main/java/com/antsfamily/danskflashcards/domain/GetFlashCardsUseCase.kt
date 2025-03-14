package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.repository.DataRepository
import com.antsfamily.danskflashcards.domain.model.LanguageType
import com.antsfamily.danskflashcards.domain.model.WordDomain
import com.antsfamily.danskflashcards.domain.model.mapToDomain
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class GetFlashCardsUseCase @Inject constructor(
    private val repository: DataRepository,
) {
    suspend operator fun invoke(type: LanguageType): List<WordDomain> =
        getFlashCardsRemote(type)

    private suspend fun getFlashCardsRemote(type: LanguageType): List<WordDomain> =
        suspendCancellableCoroutine {
            val cards = try {
                repository.getCards()
                    .mapNotNull { item -> item.mapToDomain(type) }
            } catch (e: Exception) {
                null
            }
            it.resume(cards.orEmpty())
        }
}