package com.antsfamily.danskflashcards.data.source.remote

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseHandler @Inject constructor() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun getUserWithCredentials(credential: AuthCredential) = try {
        firebaseAuth.signInWithCredential(credential).await().user
    } catch (e: Exception) {
        throw e
    }

    fun signOut() = firebaseAuth.signOut()

    fun getUser(): FirebaseUser? = firebaseAuth.currentUser

    suspend fun getUserToken(): String? = try {
        firebaseAuth.currentUser?.getIdToken(true)?.await()?.token
    } catch (e: Exception) {
        null
    }
}