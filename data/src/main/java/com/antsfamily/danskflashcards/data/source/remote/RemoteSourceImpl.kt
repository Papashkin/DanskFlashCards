package com.antsfamily.danskflashcards.data.source.remote

import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteSourceImpl @Inject constructor(
    private val firestoreHandler: FirestoreHandler
) : RemoteSource {

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