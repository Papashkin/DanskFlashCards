package com.antsfamily.danskflashcards.data.source.remote

import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface RemoteSource {
    suspend fun getUsers(): QuerySnapshot
    suspend fun getUsersFLow(): Flow<QuerySnapshot>
    suspend fun updateUser(id: String, data: HashMap<String, Any>)
}