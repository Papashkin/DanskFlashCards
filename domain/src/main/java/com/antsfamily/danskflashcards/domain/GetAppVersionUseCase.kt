package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.repository.DataRepository
import javax.inject.Inject

class GetAppVersionUseCase @Inject constructor(
    private val repository: DataRepository
) {

    operator fun invoke(): String? = repository.getAppVersion()
}