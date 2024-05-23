package com.antsfamily.danskflashcards.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.antsfamily.danskflashcards.data.PersonalBestApiModel
import com.antsfamily.danskflashcards.util.PREFERENCES_KEY_GAME_DATE
import com.antsfamily.danskflashcards.util.PREFERENCES_KEY_GAME_RESULT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetResultUseCase @Inject constructor(
    private val dataStore: DataStore<Preferences>
): FlowUseCase<Unit, PersonalBestApiModel>() {

    companion object {
        private val KEY_GAME_RESULT = intPreferencesKey(PREFERENCES_KEY_GAME_RESULT)
        private val KEY_GAME_DATE = stringPreferencesKey(PREFERENCES_KEY_GAME_DATE)
        private const val ZERO = 0
    }

    override fun run(params: Unit): Flow<PersonalBestApiModel> {
        return dataStore.data.map {
            PersonalBestApiModel(
                value = it[KEY_GAME_RESULT] ?: ZERO,
                date = it[KEY_GAME_DATE] ?: "-"
            )
        }
    }
}