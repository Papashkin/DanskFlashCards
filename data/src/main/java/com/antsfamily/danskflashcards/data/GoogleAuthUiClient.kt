package com.antsfamily.danskflashcards.data

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.antsfamily.danskflashcards.data.source.remote.FirebaseHandler
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthUiClient(
    private val context: Context,
    private val firebaseHandler: FirebaseHandler,
    private val oneTapClient: SignInClient,
    private val credentialManager: CredentialManager,
) {
    companion object {
        const val STRING_UNKNOWN_PERSON = "Unknown person"
        private const val KEY_ID_TOKEN =
            "com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID_TOKEN"
    }

    suspend fun getSignInToken(clientId: String?): String? {
        val result = try {
            getTokenId(clientId ?: return null)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
        return result
    }

    suspend fun signInWithToken(token: String?): SignInResult {
        return try {
            val googleCredentials = GoogleAuthProvider.getCredential(token, null)
            val user = firebaseHandler.getUserWithCredentials(googleCredentials)

            user?.let {
                SignInResult(
                    data = CurrentUserApiModel(
                        userId = it.uid,
                        username = it.displayName ?: STRING_UNKNOWN_PERSON,
                    ),
                    errorMessage = null
                )
            } ?: run {
                SignInResult(null, "user is empty")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    @Suppress("DEPRECATION")
    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            firebaseHandler.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    private suspend fun getTokenId(clientId: String): String? {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(getGoogleIdOption(clientId))
            .build()

        try {
            val response: GetCredentialResponse = credentialManager.getCredential(context, request)
            val googleIdCredential = response.credential
            val idToken = googleIdCredential.data.getString(KEY_ID_TOKEN)
            return idToken
        } catch (e: GetCredentialCancellationException) {
            return null
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    private fun getGoogleIdOption(clientId: String) = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(clientId)
        .setAutoSelectEnabled(false)
        .build()
}