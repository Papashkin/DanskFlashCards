package com.antsfamily.danskflashcards.data.source.remote

import com.antsfamily.danskflashcards.data.SignInResult
import com.antsfamily.danskflashcards.data.model.WordApiModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface RemoteSource {
    suspend fun getCurrentUser(): FirebaseUser?
    suspend fun getUserByID(id: String): DocumentSnapshot?
    suspend fun getWords(): List<WordApiModel>
    suspend fun getUsers(): QuerySnapshot
    suspend fun getUsersFLow(): Flow<QuerySnapshot>
    suspend fun updateUser(id: String, data: HashMap<String, Any>)

    suspend fun getGoogleSignInToken(clientId: String?): String?
    suspend fun signInWithGoogle(token: String): SignInResult
    suspend fun signOutFromGoogle()
}