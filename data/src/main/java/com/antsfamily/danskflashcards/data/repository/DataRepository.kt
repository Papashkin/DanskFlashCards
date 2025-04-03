package com.antsfamily.danskflashcards.data.repository

import com.antsfamily.danskflashcards.data.model.WordApiModel
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface DataRepository {
    fun getCurrentUser(): FirebaseUser?
    fun getAppUpdateInfo(): Task<AppUpdateInfo>
    fun startAppUpdate(updateInfo: AppUpdateInfo)
    suspend fun getWords(): List<WordApiModel>
    suspend fun getWebClientId(): String?
    suspend fun getUserByID(id: String): DocumentSnapshot?
    suspend fun getUsers(): QuerySnapshot
    suspend fun getUsersFLow(): Flow<QuerySnapshot>
    suspend fun updateUser(id: String, data: HashMap<String, Any>)
    fun getAppVersion(): String?
    suspend fun isOnboardingPassed(): Boolean
    suspend fun getLearningLanguage(): String?
    suspend fun setLearningLanguage(language: String)
    suspend fun getPrimaryLanguage(): String?
    suspend fun setPrimaryLanguage(language: String)
    suspend fun setOnboardingPassed()
}