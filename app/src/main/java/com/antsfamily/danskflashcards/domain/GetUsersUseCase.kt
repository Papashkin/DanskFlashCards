package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.model.UserApiModel
import com.antsfamily.danskflashcards.util.FirebaseConstants.COLLECTION_USERS
import com.antsfamily.danskflashcards.util.FirebaseConstants.FIELD_NAME
import com.antsfamily.danskflashcards.util.FirebaseConstants.FIELD_SCORE
import com.antsfamily.danskflashcards.util.FirebaseConstants.FIELD_TIMESTAMP
import com.antsfamily.danskflashcards.util.orZero
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GetUsersUseCase @Inject constructor(
    private val firestore: FirebaseFirestore
) : BaseUseCase<String, List<UserApiModel>>() {

    override suspend fun run(params: String): List<UserApiModel> = suspendCancellableCoroutine {
        firestore
            .collection(COLLECTION_USERS)
            .get()
            .addOnSuccessListener { snapshot ->
                val users = snapshot.map {
                    val data = it.data
                    val id = it.reference.id as? String
                    val username = data[FIELD_NAME] as? String
                    val score = data[FIELD_SCORE] as? Long
                    val date = data[FIELD_TIMESTAMP] as? Timestamp
                    UserApiModel(
                        id = id.orEmpty(),
                        username = username.orEmpty(),
                        score = score.orZero().toInt(),
                        date = date,
                        isCurrentUser = params == id
                    )
                }
                it.resume(users)
            }
            .addOnFailureListener { exception ->
                it.resumeWithException(exception)
            }
    }
}