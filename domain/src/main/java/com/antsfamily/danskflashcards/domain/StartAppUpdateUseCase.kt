package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.repository.DataRepository
import com.google.android.play.core.appupdate.AppUpdateInfo
import javax.inject.Inject

class StartAppUpdateUseCase @Inject constructor(
    private val repository: DataRepository
) {

    operator fun invoke(appUpdateInfo: AppUpdateInfo) {
        repository.startAppUpdate(appUpdateInfo)
    }
}