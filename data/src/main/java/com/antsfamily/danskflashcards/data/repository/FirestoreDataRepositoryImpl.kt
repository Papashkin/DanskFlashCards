package com.antsfamily.danskflashcards.data.repository

import com.antsfamily.danskflashcards.data.remote.FirestoreHandler
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FirestoreDataRepositoryImpl @Inject constructor(
    private val firestoreHandler: FirestoreHandler
) : FirestoreDataRepository {

    override suspend fun getUsers(): QuerySnapshot =
        firestoreHandler.getUsers()

    override suspend fun getUsersFLow(): Flow<QuerySnapshot> {
        return firestoreHandler.getUsersFlow()
    }

    override suspend fun updateUser(id: String, data: HashMap<String, Any>) =
        firestoreHandler.updateUser(id, data)
}