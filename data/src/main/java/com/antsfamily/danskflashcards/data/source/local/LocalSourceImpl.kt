package com.antsfamily.danskflashcards.data.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val PREFERENCES_KEY_LEARNING_LANG = "learning_language"
private const val PREFERENCES_KEY_PRIMARY_LANG = "primary_language"
private const val PREFERENCES_KEY_IS_ONBOARDING_PASSED = "is_onboarding_passed"

class LocalSourceImpl @Inject constructor(
    private val sharedPrefs: SecuredSharedPrefs,
    private val dataStore: DataStore<Preferences>,
    private val appVersionSource: AppVersionSource
) : LocalSource {

    companion object {
        private val KEY_LEARNING_LANG = stringPreferencesKey(PREFERENCES_KEY_LEARNING_LANG)
        private val KEY_PRIMARY_LANG = stringPreferencesKey(PREFERENCES_KEY_PRIMARY_LANG)
        private val KEY_IS_ONBOARDING_PASSED =
            booleanPreferencesKey(PREFERENCES_KEY_IS_ONBOARDING_PASSED)
    }

    override suspend fun getWebClientId(): String? {
        return sharedPrefs.getWebClientId()
    }

    override fun getAppVersion(): String? {
        return appVersionSource.getAppVersion()
    }

    override suspend fun isOnboardingPassed(): Boolean {
        return dataStore.data.map {
            it[KEY_IS_ONBOARDING_PASSED] ?: false
        }.first()
    }

    override suspend fun getLearningLanguage(): String? {
        return dataStore.data.map {
            it[KEY_LEARNING_LANG]
        }.first()
    }

    override suspend fun setLearningLanguage(language: String) {
        dataStore.edit {
            it[KEY_LEARNING_LANG] = language
        }
    }

    override suspend fun getPrimaryLanguage(): String? {
        return dataStore.data.map {
            it[KEY_PRIMARY_LANG]
        }.first()
    }

    override suspend fun setPrimaryLanguage(language: String) {
        dataStore.edit {
            it[KEY_PRIMARY_LANG] = language
        }
    }

    override suspend fun setOnboardingPassed() {
        dataStore.edit {
            it[KEY_IS_ONBOARDING_PASSED] = true
        }
    }
}