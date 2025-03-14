package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.BeginSignInResult
import javax.inject.Inject

class GetSignInResultUseCase @Inject constructor(
    private val authClient: GoogleAuthUiClient
) {
    suspend operator fun invoke(): BeginSignInResult? = authClient.getSignInResult()
}
