package com.antsfamily.danskflashcards.data

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

private const val ID_FIREBASE_CLIENT =
    "692133800678-v9uijmnu84efrqtpgf7ql2kua9cl2afa.apps.googleusercontent.com"

class GoogleAuthUiClient(
    private val oneTapClient: SignInClient,
) {
    private val auth = Firebase.auth

    suspend fun getSignInResult(): BeginSignInResult? {
        val result = try {
            oneTapClient.beginSignIn(buildSignInRequest()).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result
    }

    suspend fun signInWithToken(token: String?): SignInResult {
        val googleCredentials = GoogleAuthProvider.getCredential(token, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user

            user?.let {
                SignInResult(
                    data = CurrentUserApiModel(
                        userId = it.uid,
                        username = it.displayName.orEmpty(),
                        email = it.email.orEmpty()
                    ),
                    errorMessage = null
                )
            } ?: run {
                SignInResult(null, "user is empty")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(null, e.message)
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(ID_FIREBASE_CLIENT)
                    .build()
            )
            .setAutoSelectEnabled(false)
            .build()
    }
}