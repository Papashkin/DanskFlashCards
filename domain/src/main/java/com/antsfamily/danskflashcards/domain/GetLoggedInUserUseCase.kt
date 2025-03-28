package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.CurrentUserApiModel
import com.antsfamily.danskflashcards.data.GoogleAuthUiClient.Companion.STRING_UNKNOWN_PERSON
import com.antsfamily.danskflashcards.data.source.remote.FirebaseHandler
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class GetLoggedInUserUseCase @Inject constructor(
    private val firebaseHandler: FirebaseHandler
) {

    suspend operator fun invoke(): CurrentUserApiModel? = suspendCancellableCoroutine {
        val user = firebaseHandler.getUser()?.let { user ->
            CurrentUserApiModel(
                userId = user.uid,
                username = user.displayName ?: STRING_UNKNOWN_PERSON,
            )
        }
        it.resume(user)
    }
}