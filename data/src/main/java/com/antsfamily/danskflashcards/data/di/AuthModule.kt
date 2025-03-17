package com.antsfamily.danskflashcards.data.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.antsfamily.danskflashcards.data.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
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
    fun provideCredentialManager(@ApplicationContext context: Context) =
        CredentialManager.create(context)

    @Singleton
    @Provides
    fun provideGoogleSignInClient(@ApplicationContext context: Context): SignInClient =
        Identity.getSignInClient(context)

    @Singleton
    @Provides
    fun provideGoogleAuthClient(
        @ApplicationContext context: Context,
        client: SignInClient,
        credentialManager: CredentialManager
    ): GoogleAuthUiClient = GoogleAuthUiClient(context, client, credentialManager)
}