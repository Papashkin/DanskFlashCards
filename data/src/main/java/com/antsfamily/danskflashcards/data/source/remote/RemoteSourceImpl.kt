package com.antsfamily.danskflashcards.data.source.remote

import com.antsfamily.danskflashcards.data.model.WordApiModel
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteSourceImpl @Inject constructor(
    private val firestoreHandler: FirestoreHandler,
    private val firebaseHandler: FirebaseHandler,
    private val updateManager: UpdateManager,
    private val service: WordsApiService
) : RemoteSource {

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseHandler.getUser()
    }

    override fun getAppUpdateInfo(): Task<AppUpdateInfo> {
        return updateManager.getAppInfo()
    }

    override fun startAppUpdate(updateInfo: AppUpdateInfo) {
        return updateManager.startUpdateFlow(updateInfo)
    }

    override suspend fun getUserByID(id: String): DocumentSnapshot? {
        return firestoreHandler.getUserByID(id)
    }

    override suspend fun getWords(): List<WordApiModel> {
        return service.getWords()
    }

    override suspend fun getUsers(): QuerySnapshot {
        return firestoreHandler.getUsers()
    }

    override suspend fun getUsersFLow(): Flow<QuerySnapshot> {
        return firestoreHandler.getUsersFlow()
    }

    override suspend fun updateUser(id: String, data: HashMap<String, Any>) {
        return firestoreHandler.updateUser(id, data)
    }
}