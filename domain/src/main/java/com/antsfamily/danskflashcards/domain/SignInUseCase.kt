package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.CurrentUserApiModel
import com.antsfamily.danskflashcards.data.GoogleAuthUiClient
import com.antsfamily.danskflashcards.data.model.SignInType
import com.antsfamily.danskflashcards.data.repository.DataRepository
import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_NAME
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val client: GoogleAuthUiClient,
    private val dataRepository: DataRepository
) {
    suspend operator fun invoke(type: SignInType): CurrentUserApiModel? {
        return try {
            val token = client.getSignInToken(type, dataRepository.getWebClientId())
            if (token != null) {
                val signInResult = client.signInWithToken(token)
                signInResult.data?.let {
                    val userData = hashMapOf<String, Any>(FIELD_NAME to it.username)
                    dataRepository.updateUser(it.userId, userData)
                }
                return signInResult.data
            } else {
                null
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
