package com.antsfamily.danskflashcards.domain

import com.antsfamily.danskflashcards.data.model.UserApiModel
import com.antsfamily.danskflashcards.util.FirebaseConstants.COLLECTION_USERS
import com.antsfamily.danskflashcards.util.FirebaseConstants.FIELD_NAME
import com.antsfamily.danskflashcards.util.FirebaseConstants.FIELD_SCORE
import com.antsfamily.danskflashcards.util.FirebaseConstants.FIELD_TIMESTAMP
import com.antsfamily.danskflashcards.util.orZero
import com.antsfamily.danskflashcards.util.toIsoString
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UserUpdateFLowUseCase @Inject constructor(
    private val firestore: FirebaseFirestore
) : FlowUseCase<String, List<UserApiModel>>() {

    override fun run(params: String): Flow<List<UserApiModel>> = callbackFlow {
        val documentRef = firestore
            .collection(COLLECTION_USERS)

        val listenerRegistration = documentRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val users = snapshot?.mapNotNull {
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
            if (!users.isNullOrEmpty()) {
                trySend(users).isSuccess
            }
        }

        awaitClose { listenerRegistration.remove() }
    }
}
