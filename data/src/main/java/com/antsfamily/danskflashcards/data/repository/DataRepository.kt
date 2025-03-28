package com.antsfamily.danskflashcards.data.repository

import com.antsfamily.danskflashcards.data.model.WordApiModel
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface DataRepository {
    fun getAppUpdateInfo(): Task<AppUpdateInfo>
    fun startAppUpdate(updateInfo: AppUpdateInfo)
    suspend fun getWords(): List<WordApiModel>
    suspend fun getWebClientId(): String?
    suspend fun getUsers(): QuerySnapshot
    suspend fun getUsersFLow(): Flow<QuerySnapshot>
    suspend fun updateUser(id: String, data: HashMap<String, Any>)
    fun getAppVersion(): String?
    suspend fun isOnboardingPassed(): Boolean
    suspend fun getSelectedLanguage(): String?
    suspend fun setSelectedLanguage(language: String)
    suspend fun setOnboardingPassed()
}