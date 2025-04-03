package com.antsfamily.danskflashcards.data.source.remote

import com.antsfamily.danskflashcards.data.model.WordApiModel
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface RemoteSource {
    fun getCurrentUser(): FirebaseUser?
    fun getAppUpdateInfo(): Task<AppUpdateInfo>
    fun startAppUpdate(updateInfo: AppUpdateInfo)
    suspend fun getUserByID(id: String): DocumentSnapshot?
    suspend fun getWords(): List<WordApiModel>
    suspend fun getUsers(): QuerySnapshot
    suspend fun getUsersFLow(): Flow<QuerySnapshot>
    suspend fun updateUser(id: String, data: HashMap<String, Any>)
}