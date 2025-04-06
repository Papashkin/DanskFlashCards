package com.antsfamily.danskflashcards.domain.repository

import com.antsfamily.danskflashcards.domain.model.UserDomain

interface AuthRepository {
    suspend fun getGoogleSignInToken(clientId: String?): String?
    suspend fun signInWithGoogle(token: String): UserDomain?
    suspend fun signOutFromGoogle()
}