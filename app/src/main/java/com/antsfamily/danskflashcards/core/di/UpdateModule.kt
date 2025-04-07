package com.antsfamily.danskflashcards.core.di

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UpdateModule {

    @Singleton
    @Provides
    fun provideAppUpdateManager(@ApplicationContext context: Context) =
        AppUpdateManagerFactory.create(context)
}