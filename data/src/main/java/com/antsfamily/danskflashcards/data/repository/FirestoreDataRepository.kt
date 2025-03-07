package com.antsfamily.danskflashcards.data.repository

import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface FirestoreDataRepository {
    suspend fun getUsers(): QuerySnapshot
    suspend fun getUsersFLow(): Flow<QuerySnapshot>
    suspend fun updateUser(id: String, data: HashMap<String, Any>)
}