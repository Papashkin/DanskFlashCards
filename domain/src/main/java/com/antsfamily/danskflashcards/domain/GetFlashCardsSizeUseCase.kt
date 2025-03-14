package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.repository.DataRepository
import com.antsfamily.danskflashcards.data.util.orZero
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class GetFlashCardsSizeUseCase @Inject constructor(
    private val repository: DataRepository,
) {
    private var cardsAmount: Int? = null

    suspend operator fun invoke(): Int = cardsAmount ?: getFlashCardsAmount()

    private suspend fun getFlashCardsAmount(): Int = suspendCancellableCoroutine {
        val cardsAmount = try {
            repository.getCardsAmount()
        } catch (e: Exception) {
            null
        }
        it.resume(cardsAmount.orZero())
    }
}