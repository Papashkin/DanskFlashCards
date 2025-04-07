package com.antsfamily.danskflashcards.data.source.remote

import com.antsfamily.danskflashcards.data.util.FirebaseConstants.COLLECTION_USERS
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreHandler @Inject constructor() {

    private val firestore: FirebaseFirestore = Firebase.firestore

    suspend fun getUserByID(id: String): DocumentSnapshot? {
        try {
            val data = firestore.collection(COLLECTION_USERS).document(id).get().await()
            return data
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getUsers(): QuerySnapshot {
        try {
            val data = firestore.collection(COLLECTION_USERS).get().await()
            return data
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updateUser(id: String, data: HashMap<String, Any>) {
        try {
            firestore
                .collection(COLLECTION_USERS)
                .document(id)
                .set(data, SetOptions.merge())
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    //TODO add cancellation for that flow in Logout case
    fun getUsersFlow() = callbackFlow {
        val documentRef = firestore
            .collection(COLLECTION_USERS)

        val listenerRegistration = documentRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                trySend(snapshot).isSuccess
            }
        }
        awaitClose { listenerRegistration.remove() }
    }
}