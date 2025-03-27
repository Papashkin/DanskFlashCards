package com.antsfamily.danskflashcards.data.di

import android.content.Context
import com.antsfamily.danskflashcards.data.source.local.AppVersionSource
import com.antsfamily.danskflashcards.data.source.local.SharedPrefs
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
    fun provideSharedPrefs(@ApplicationContext context: Context) =
        SharedPrefs(context = context)

    @Singleton
    @Provides
    fun provideAppVersionSource(@ApplicationContext context: Context) =
        AppVersionSource(context = context)

    @Singleton
    @Provides
    fun provideFirestoreHandler(): FirestoreHandler = FirestoreHandler()
}