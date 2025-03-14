package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.GoogleAuthUiClient
import com.antsfamily.danskflashcards.data.SignInResult
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val client: GoogleAuthUiClient
) {
    suspend operator fun invoke(googleIdToken: String): SignInResult {
        return client.signInWithToken(googleIdToken)
    }
}
