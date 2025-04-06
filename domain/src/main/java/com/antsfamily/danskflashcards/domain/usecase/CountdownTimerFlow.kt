package com.antsfamily.danskflashcards.domain.usecase

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CountdownTimerFlow @Inject constructor() {

    fun run(params: Long): Flow<Unit> = flow {
        while (true) {
            emit(Unit)
            delay(params)
        }
    }
}