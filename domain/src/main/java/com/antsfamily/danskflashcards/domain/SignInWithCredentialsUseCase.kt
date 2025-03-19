package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.GoogleAuthUiClient
import com.antsfamily.danskflashcards.data.SignInResult
import com.antsfamily.danskflashcards.data.model.SignInType
import com.antsfamily.danskflashcards.data.repository.DataRepository
import javax.inject.Inject

class SignInWithCredentialsUseCase @Inject constructor(
    private val client: GoogleAuthUiClient,
    private val dataRepository: DataRepository
) {
    suspend operator fun invoke(type: SignInType): SignInResult {
        return try {
            val token = client.getSignInToken(type, dataRepository.getWebClientId())
            token?.let {
                client.signInWithToken(it)
            } ?: run {
                 throw Error(Exception("Token is empty"))
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
