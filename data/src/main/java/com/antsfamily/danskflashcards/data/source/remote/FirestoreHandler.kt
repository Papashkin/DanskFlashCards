package com.antsfamily.danskflashcards.data.source.remote

import com.antsfamily.danskflashcards.data.util.FirebaseConstants.COLLECTION_USERS
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirestoreHandler @Inject constructor() {

    private val firestore: FirebaseFirestore = Firebase.firestore

    suspend fun getUsers(): QuerySnapshot = suspendCancellableCoroutine {
        firestore
            .collection(COLLECTION_USERS)
            .get()
            .addOnSuccessListener { snapshot ->
                it.resume(snapshot)
            }
            .addOnFailureListener { exception ->
                it.resumeWithException(exception)
            }
    }

    suspend fun updateUser(id: String, data: HashMap<String, Any>): Unit = suspendCancellableCoroutine {
        firestore
            .collection(COLLECTION_USERS)
            .document(id)
            .set(data)
            .addOnSuccessListener { _ ->
                it.resume(Unit)
            }
            .addOnFailureListener { exception ->
                it.resumeWithException(exception)
            }
    }

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