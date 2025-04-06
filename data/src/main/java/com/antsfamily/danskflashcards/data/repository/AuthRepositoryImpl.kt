package com.antsfamily.danskflashcards.data.repository

import com.antsfamily.danskflashcards.data.source.remote.RemoteSource
import com.antsfamily.danskflashcards.domain.model.UserDomain
import com.antsfamily.danskflashcards.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteSource: RemoteSource,
): AuthRepository {

    override suspend fun getGoogleSignInToken(clientId: String?): String? {
        return remoteSource.getGoogleSignInToken(clientId)
    }

    override suspend fun signInWithGoogle(token: String): UserDomain? {
        val result = remoteSource.signInWithGoogle(token)
        return result.data?.let {
            UserDomain(
                id = it.userId,
                username = it.username,
                score = 0,
                isCurrentUser = true,
                avatarId = null
            )
        }
    }

    override suspend fun signOutFromGoogle() {
        return remoteSource.signOutFromGoogle()
    }
}