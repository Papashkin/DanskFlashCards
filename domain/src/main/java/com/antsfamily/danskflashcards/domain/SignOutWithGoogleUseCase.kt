package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.GoogleAuthUiClient
import javax.inject.Inject

class SignOutWithGoogleUseCase @Inject constructor(
    private val client: GoogleAuthUiClient,
) {

    suspend operator fun invoke() {
        return client.signOut()
    }
}

