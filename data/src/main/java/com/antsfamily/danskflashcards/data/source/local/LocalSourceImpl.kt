package com.antsfamily.danskflashcards.data.source.local

import javax.inject.Inject

class LocalSourceImpl @Inject constructor(
    private val sharedPrefs: SharedPrefs,
    private val appVersionSource: AppVersionSource
) : LocalSource {

    override suspend fun getWebClientId(): String? {
        return sharedPrefs.getWebClientId()
    }

    override fun getAppVersion(): String? {
        return appVersionSource.getAppVersion()
    }
}