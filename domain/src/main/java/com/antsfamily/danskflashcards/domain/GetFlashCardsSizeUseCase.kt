package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.repository.DataRepository
import com.antsfamily.danskflashcards.data.util.orZero
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GetFlashCardsSizeUseCase @Inject constructor(
    private val repository: DataRepository,
) {
    private var cardsAmount: Int? = null

    suspend operator fun invoke(): Int = cardsAmount ?: getFlashCardsAmount()

    private suspend fun getFlashCardsAmount(): Int = suspendCancellableCoroutine {
        try {
            val cardsAmount = repository.getCardsAmount()
            it.resume(cardsAmount.orZero())
        } catch (e: Exception) {
            e.printStackTrace()
            it.resumeWithException(e)
        }
    }
}