package com.antsfamily.danskflashcards.data.repository

import com.antsfamily.danskflashcards.data.model.WordApiModel
import com.antsfamily.danskflashcards.data.source.local.LocalSource
import com.antsfamily.danskflashcards.data.source.remote.RemoteSource
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val localSource: LocalSource,
    private val remoteSource: RemoteSource,
): DataRepository {

    override fun getAppUpdateInfo(): Task<AppUpdateInfo> {
        return remoteSource.getAppUpdateInfo()
    }

    override fun startAppUpdate(updateInfo: AppUpdateInfo) {
        return remoteSource.startAppUpdate(updateInfo)
    }

    override suspend fun getWords(): List<WordApiModel> {
        return remoteSource.getWords()
    }

    override suspend fun getWebClientId(): String? {
        return localSource.getWebClientId()
    }

    override suspend fun getUsers(): QuerySnapshot {
        return remoteSource.getUsers()
    }

    override suspend fun getUsersFLow(): Flow<QuerySnapshot> {
        return remoteSource.getUsersFLow()
    }

    override suspend fun updateUser(id: String, data: HashMap<String, Any>) {
        return remoteSource.updateUser(id, data)
    }

    override fun getAppVersion(): String? {
        return localSource.getAppVersion()
    }
}