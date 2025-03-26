package com.antsfamily.danskflashcards.data.source.remote

import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface RemoteSource {
    fun getAppUpdateInfo(): Task<AppUpdateInfo>
    fun startAppUpdate(updateInfo: AppUpdateInfo)
    suspend fun getUsers(): QuerySnapshot
    suspend fun getUsersFLow(): Flow<QuerySnapshot>
    suspend fun updateUser(id: String, data: HashMap<String, Any>)
}