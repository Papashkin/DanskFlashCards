package com.antsfamily.danskflashcards.data.di

import com.antsfamily.danskflashcards.data.repository.AuthRepositoryImpl
import com.antsfamily.danskflashcards.data.repository.DataRepositoryImpl
import com.antsfamily.danskflashcards.data.source.local.LocalSource
import com.antsfamily.danskflashcards.data.source.local.LocalSourceImpl
import com.antsfamily.danskflashcards.data.source.remote.RemoteSource
import com.antsfamily.danskflashcards.data.source.remote.RemoteSourceImpl
import com.antsfamily.danskflashcards.domain.repository.AuthRepository
import com.antsfamily.danskflashcards.domain.repository.DataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindsAuthRepository(repositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindsDataRepository(repositoryImpl: DataRepositoryImpl): DataRepository

    @Binds
    abstract fun bindsLocalSource(localSourceImpl: LocalSourceImpl): LocalSource

    @Binds
    abstract fun bindsRemoteSource(remoteSourceImpl: RemoteSourceImpl): RemoteSource
}
