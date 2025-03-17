package com.antsfamily.danskflashcards.data

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.antsfamily.danskflashcards.data.model.SignInType
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

private const val WEB_CLIENT_ID =
    "692133800678-v9uijmnu84efrqtpgf7ql2kua9cl2afa.apps.googleusercontent.com"
private const val KEY_ID_TOKEN =
    "com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID_TOKEN"

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient,
    private val credentialManager: CredentialManager,
) {
    private val auth = Firebase.auth

    suspend fun getSignInToken(type: SignInType): String? {
        val result = try {
            getTokenId(type)
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result
    }

    suspend fun signInWithToken(token: String?): SignInResult {
        return try {
            val googleCredentials = GoogleAuthProvider.getCredential(token, null)
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

    @Suppress("DEPRECATION")
    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    private suspend fun getTokenId(type: SignInType): String? {
        val request = when (type) {
            SignInType.GOOGLE -> GetCredentialRequest.Builder()
                .addCredentialOption(getGoogleIdOption())
                .build()

            SignInType.FACEBOOK -> null //TODO should be implemented later and separately
        }

        try {
            val response: GetCredentialResponse =
                credentialManager.getCredential(context, request ?: return null)
            val googleIdCredential = response.credential
            val idToken = googleIdCredential.data.getString(KEY_ID_TOKEN)
            return idToken
        } catch (e: GetCredentialException) {
            return null
        }
    }

    private fun getGoogleIdOption() = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(WEB_CLIENT_ID)
        .setAutoSelectEnabled(false)
        .build()
}