package com.antsfamily.danskflashcards.data.source.remote

import android.app.Activity
import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.model.AppUpdateType
import javax.inject.Inject

class UpdateManager @Inject constructor(
    private val context: Context,
    private val appUpdateManager: AppUpdateManager,
) {

    fun getAppInfo() = appUpdateManager.appUpdateInfo

    @Suppress("DEPRECATION")
    fun startUpdateFlow(updateInfo: AppUpdateInfo) {
        appUpdateManager.startUpdateFlowForResult(
            updateInfo,
            AppUpdateType.IMMEDIATE,
            context as Activity,
            100
        )
    }
}