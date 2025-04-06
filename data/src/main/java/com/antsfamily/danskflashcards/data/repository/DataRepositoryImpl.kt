package com.antsfamily.danskflashcards.data.repository

import com.antsfamily.danskflashcards.data.model.mapToDomain
import com.antsfamily.danskflashcards.data.source.local.LocalSource
import com.antsfamily.danskflashcards.data.source.remote.RemoteSource
import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_AVATAR_ID
import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_NAME
import com.antsfamily.danskflashcards.data.util.FirebaseConstants.FIELD_SCORE
import com.antsfamily.danskflashcards.data.util.mapToDomain
import com.antsfamily.danskflashcards.domain.model.UserDomain
import com.antsfamily.danskflashcards.domain.model.WordDomain
import com.antsfamily.danskflashcards.domain.repository.DataRepository
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val localSource: LocalSource,
    private val remoteSource: RemoteSource,
) : DataRepository {

    override suspend fun getCurrentUser(): UserDomain? {
        return remoteSource.getCurrentUser()?.let { user ->
            remoteSource.getUserByID(user.uid).mapToDomain(user.uid)
        }
    }

    override fun getAppUpdateInfo(): Task<AppUpdateInfo> {
        return remoteSource.getAppUpdateInfo()
    }

    override fun startAppUpdate(updateInfo: AppUpdateInfo) {
        return remoteSource.startAppUpdate(updateInfo)
    }

    override suspend fun getUserByID(id: String): UserDomain? {
        return remoteSource.getUserByID(id).mapToDomain(id)
    }

    override suspend fun getWords(): List<WordDomain> {
        return remoteSource.getWords().mapToDomain()
    }

    override suspend fun getWebClientId(): String? {
        return localSource.getWebClientId()
    }

    override suspend fun getUsers(): List<UserDomain> {
        return remoteSource.getUsers().mapToDomain()
    }

    override suspend fun getUsersFLow(): Flow<List<UserDomain>> {
        return remoteSource.getUsersFLow().map {
            it.mapToDomain(getCurrentUser()?.id.orEmpty())
        }
    }

    override suspend fun updateUserScore(id: String, name: String, score: Int) {
        val userData = hashMapOf<String, Any>(
            FIELD_NAME to name,
            FIELD_SCORE to score,
        )
        return remoteSource.updateUser(id, userData)
    }

    override suspend fun updateUserName(id: String, name: String) {
        val userData = hashMapOf<String, Any>(FIELD_NAME to name)
        return remoteSource.updateUser(id, userData)
    }

    override suspend fun updateUserAvatar(id: String, avatarId: Int) {
        val userData = hashMapOf<String, Any>(FIELD_AVATAR_ID to avatarId)
        return remoteSource.updateUser(id, userData)
    }

    override fun getAppVersion(): String? {
        return localSource.getAppVersion()
    }

    override suspend fun isOnboardingPassed(): Boolean {
        return localSource.isOnboardingPassed()
    }

    override suspend fun getLearningLanguage(): String? {
        return localSource.getLearningLanguage()
    }

    override suspend fun setLearningLanguage(language: String) {
        return localSource.setLearningLanguage(language)
    }

    override suspend fun getPrimaryLanguage(): String? {
        return localSource.getPrimaryLanguage()
    }

    override suspend fun setPrimaryLanguage(language: String) {
        return localSource.setPrimaryLanguage(language)
    }

    override suspend fun setOnboardingPassed() {
        return localSource.setOnboardingPassed()
    }
}