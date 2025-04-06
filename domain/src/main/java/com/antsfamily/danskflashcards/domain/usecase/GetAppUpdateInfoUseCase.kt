package com.antsfamily.danskflashcards.domain.usecase

import com.antsfamily.danskflashcards.domain.repository.DataRepository
import com.google.android.play.core.appupdate.AppUpdateInfo
import javax.inject.Inject

class GetAppUpdateInfoUseCase @Inject constructor(
    private val repository: DataRepository
) {

    operator fun invoke(): AppUpdateInfo {
        try {
            val updateInfo = repository.getAppUpdateInfo()
            return updateInfo.result
        } catch (e: Exception) {
            throw e
        }
    }
}