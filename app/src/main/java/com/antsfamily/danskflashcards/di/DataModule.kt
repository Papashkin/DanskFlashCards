package com.antsfamily.danskflashcards.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.antsfamily.danskflashcards.data.LocalDataStore
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideLocalDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.LocalDataStore
    }

    @Singleton
    @Provides
    fun provideGson(): Gson = Gson()
}