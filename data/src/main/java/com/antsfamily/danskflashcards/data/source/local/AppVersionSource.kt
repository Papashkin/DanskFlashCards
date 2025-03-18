package com.antsfamily.danskflashcards.data.source.local

import android.content.Context
import android.os.Build
import javax.inject.Inject

class AppVersionSource @Inject constructor(private val context: Context) {

    fun getAppVersion(): String? {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return packageInfo.versionName?.let {
            val code = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode.toString()
            } else {
                packageInfo.versionCode.toString()
            }
            it.plus(" ($code)")
        }
    }
}