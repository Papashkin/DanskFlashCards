package com.antsfamily.danskflashcards.domain.usecase

import com.antsfamily.danskflashcards.domain.model.SignInType
import com.antsfamily.danskflashcards.domain.model.UserDomain
import com.antsfamily.danskflashcards.domain.repository.AuthRepository
import com.antsfamily.danskflashcards.domain.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val dataRepository: DataRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(type: SignInType): UserDomain? = withContext(Dispatchers.IO) {
        val result = when (type) {
            SignInType.GOOGLE -> signInWithGoogle()
            SignInType.FACEBOOK -> null
        }

        if (result?.id != null) {
            val allUsersIDs = dataRepository.getUsers().map { it.id }
            if (result.id !in allUsersIDs) {
                dataRepository.updateUserName(result.id, result.username)
            }
        }

        result
    }

    private suspend fun signInWithGoogle(): UserDomain? {
        val clientId = dataRepository.getWebClientId()
        val token = authRepository.getGoogleSignInToken(clientId)
        return if (token != null) {
            val result = authRepository.signInWithGoogle(token)
            result
        } else {
            null
        }
    }
}
