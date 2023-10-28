package com.antsfamily.danskflashcards.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.antsfamily.danskflashcards.util.PREFERENCES_KEY_GAME_RESULT
import javax.inject.Inject

class SaveResultUseCase @Inject constructor(
    private val dataStore: DataStore<Preferences>
): BaseUseCase<Int, Unit>() {

    companion object {
        private val KEY_GAME_RESULT = intPreferencesKey(PREFERENCES_KEY_GAME_RESULT)
    }

    override suspend fun run(params: Int) {
        dataStore.edit {
            it[KEY_GAME_RESULT] = params
        }
    }
}