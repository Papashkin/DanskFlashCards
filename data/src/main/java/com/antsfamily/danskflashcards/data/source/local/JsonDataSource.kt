package com.antsfamily.danskflashcards.data.source.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class JsonDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun readJsonFile(fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
}