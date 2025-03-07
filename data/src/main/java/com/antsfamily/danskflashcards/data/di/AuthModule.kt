package com.antsfamily.danskflashcards.data.di

import android.content.Context
import com.antsfamily.danskflashcards.data.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.Identity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Singleton
    @Provides
    fun provideGoogleAuthClient(@ApplicationContext context: Context): GoogleAuthUiClient =
        GoogleAuthUiClient(oneTapClient = Identity.getSignInClient(context))
}