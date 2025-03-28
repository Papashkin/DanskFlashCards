package com.antsfamily.danskflashcards.data.source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.LocalDataStore: DataStore<Preferences> by preferencesDataStore("flashcards")
