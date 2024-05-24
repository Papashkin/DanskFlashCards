package com.antsfamily.danskflashcards.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.antsfamily.danskflashcards.util.PREFERENCES_KEY_GAME_DATE
import com.antsfamily.danskflashcards.util.PREFERENCES_KEY_GAME_RESULT
import com.antsfamily.danskflashcards.util.toString
import java.util.Calendar
import javax.inject.Inject

class SetPersonalBestUseCase @Inject constructor(
    private val dataStore: DataStore<Preferences>
): BaseUseCase<Int, Unit>() {

    companion object {
        private val KEY_GAME_RESULT = intPreferencesKey(PREFERENCES_KEY_GAME_RESULT)
        private val KEY_GAME_DATE = stringPreferencesKey(PREFERENCES_KEY_GAME_DATE)
    }

    override suspend fun run(params: Int) {
        val currentTime = Calendar.getInstance().time.toString("E, dd MMM yyyy HH:mm")
        dataStore.edit {
            it[KEY_GAME_RESULT] = params
            it[KEY_GAME_DATE] = currentTime
        }
    }
}