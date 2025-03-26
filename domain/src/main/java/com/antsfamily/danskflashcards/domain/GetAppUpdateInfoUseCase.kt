package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.repository.DataRepository
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GetAppUpdateInfoUseCase @Inject constructor(
    private val repository: DataRepository
) {

    suspend operator fun invoke(): AppUpdateInfo? = suspendCancellableCoroutine {
        repository.getAppUpdateInfo().addOnSuccessListener { updateInfo ->
            val isUpdateAvailable =
                (updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
            if (isUpdateAvailable) {
                it.resume(updateInfo)
            } else {
                it.resume(null)
            }
        }.addOnCanceledListener {
            it.resume(null)
        }.addOnFailureListener { e ->
            it.resumeWithException(e)
        }
    }
}