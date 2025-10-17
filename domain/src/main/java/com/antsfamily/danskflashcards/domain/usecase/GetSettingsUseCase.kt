package com.antsfamily.danskflashcards.domain.usecase

import com.antsfamily.danskflashcards.domain.model.LanguageType
import com.antsfamily.danskflashcards.domain.model.Settings
import com.antsfamily.danskflashcards.domain.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    companion object {
        const val EXCEPTION_MSG_EMPTY_USER = "user data is empty"
    }

    suspend operator fun invoke(): Settings = withContext(Dispatchers.IO) {
        val user = repository.getCurrentUser()
        val appVersion = repository.getAppVersion()
        val primaryLang = getPrimaryLanguage()
        val learningLang = getLearningLanguage()

        if (user == null) throw Exception(EXCEPTION_MSG_EMPTY_USER)
        if (appVersion == null) throw Exception(EXCEPTION_MSG_EMPTY_USER)

        Settings(
            userId = user.id,
            username = user.username,
            learningLanguage = learningLang,
            primaryLanguage = primaryLang,
            avatarId = user.avatarId,
            appVersion = appVersion
        )
    }

    private suspend fun getLearningLanguage(): LanguageType {
        val languageName = repository.getLearningLanguage()
        return languageName?.let { LanguageType.valueOf(languageName) } ?: LanguageType.EN
    }

    private suspend fun getPrimaryLanguage(): LanguageType {
        val languageName = repository.getPrimaryLanguage()
        return languageName?.let { LanguageType.valueOf(languageName) } ?: LanguageType.DK
    }
}