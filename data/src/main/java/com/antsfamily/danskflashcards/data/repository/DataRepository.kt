package com.antsfamily.danskflashcards.data.repository

import com.antsfamily.danskflashcards.data.model.WordApiModel
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface DataRepository {
    fun getCards(): List<WordApiModel>
    fun getCardsAmount(): Int
    suspend fun getUsers(): QuerySnapshot
    suspend fun getUsersFLow(): Flow<QuerySnapshot>
    suspend fun updateUser(id: String, data: HashMap<String, Any>)
}