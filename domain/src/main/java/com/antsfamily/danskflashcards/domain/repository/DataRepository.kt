package com.antsfamily.danskflashcards.domain.repository

import com.antsfamily.danskflashcards.domain.model.UserDomain
import com.antsfamily.danskflashcards.domain.model.WordDomain
import kotlinx.coroutines.flow.Flow

interface DataRepository {
    suspend fun getCurrentUser(): UserDomain?
    suspend fun getWords(): List<WordDomain>
    suspend fun getWebClientId(): String?
    suspend fun getUsers(): List<UserDomain>
    suspend fun getUsersFLow(): Flow<List<UserDomain>>
    suspend fun updateUserScore(id: String, score: Int)
    suspend fun updateUserName(id: String, name: String)
    suspend fun updateUserAvatar(id: String, avatarId: Int)
    fun getAppVersion(): String?
    suspend fun isOnboardingPassed(): Boolean
    suspend fun getLearningLanguage(): String?
    suspend fun setLearningLanguage(language: String)
    suspend fun getPrimaryLanguage(): String?
    suspend fun setPrimaryLanguage(language: String)
    suspend fun setOnboardingPassed()
}