package com.antsfamily.danskflashcards.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.antsfamily.danskflashcards.util.PREFERENCES_KEY_GAME_RESULT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetResultUseCase @Inject constructor(
    private val dataStore: DataStore<Preferences>
): FlowUseCase<Unit, Int>() {

    companion object {
        private val KEY_GAME_RESULT = intPreferencesKey(PREFERENCES_KEY_GAME_RESULT)
        private const val ZERO = 0
    }

    override fun run(params: Unit): Flow<Int> {
        return dataStore.data.map {
            it[KEY_GAME_RESULT] ?: ZERO
        }
    }
}