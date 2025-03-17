package com.antsfamily.danskflashcards.data.repository

import com.antsfamily.danskflashcards.data.model.WordApiModel
import com.antsfamily.danskflashcards.data.source.local.LocalSource
import com.antsfamily.danskflashcards.data.source.remote.RemoteSource
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val localSource: LocalSource,
    private val remoteSource: RemoteSource,
): DataRepository {

    override fun getCards(): List<WordApiModel> {
        return localSource.getCards()
    }

    override fun getCardsAmount(): Int {
        return localSource.getCardsAmount()
    }

    override suspend fun getWebClientId(): String? {
        return localSource.getWebClientId()
    }

    override suspend fun getUsers(): QuerySnapshot {
        return remoteSource.getUsers()
    }

    override suspend fun getUsersFLow(): Flow<QuerySnapshot> {
        return remoteSource.getUsersFLow()
    }

    override suspend fun updateUser(id: String, data: HashMap<String, Any>) {
        return remoteSource.updateUser(id, data)
    }
}