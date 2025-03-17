package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.GoogleAuthUiClient
import com.antsfamily.danskflashcards.data.SignInResult
import com.antsfamily.danskflashcards.data.model.SignInType
import javax.inject.Inject

class SignInWithCredentialsUseCase @Inject constructor(
    private val client: GoogleAuthUiClient
) {
    suspend operator fun invoke(type: SignInType): SignInResult? {
        try {
            val token = client.getSignInToken(type)
            return token?.let {
                client.signInWithToken(it)
            }
        } catch (e: Exception) {
            return null
        }
    }
}
