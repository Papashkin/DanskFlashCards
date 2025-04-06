package com.antsfamily.danskflashcards.domain.usecase

import com.antsfamily.danskflashcards.domain.repository.DataRepository
import javax.inject.Inject

class GetAppVersionUseCase @Inject constructor(
    private val repository: DataRepository
) {

    operator fun invoke(): String? = repository.getAppVersion()
}