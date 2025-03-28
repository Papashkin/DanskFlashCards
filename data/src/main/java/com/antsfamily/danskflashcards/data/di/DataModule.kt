package com.antsfamily.danskflashcards.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.antsfamily.danskflashcards.data.source.local.AppVersionSource
import com.antsfamily.danskflashcards.data.source.local.LocalDataStore
import com.antsfamily.danskflashcards.data.source.local.SecuredSharedPrefs
import com.antsfamily.danskflashcards.data.source.remote.FirestoreHandler
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
    fun provideLocalDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.LocalDataStore

    @Singleton
    @Provides
    fun provideSecuredSharedPrefs(@ApplicationContext context: Context) =
        SecuredSharedPrefs(context = context)

    @Singleton
    @Provides
    fun provideAppVersionSource(@ApplicationContext context: Context) =
        AppVersionSource(context = context)

    @Singleton
    @Provides
    fun provideFirestoreHandler(): FirestoreHandler = FirestoreHandler()
}