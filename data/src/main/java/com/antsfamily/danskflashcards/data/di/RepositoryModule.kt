package com.antsfamily.danskflashcards.data.di

import com.antsfamily.danskflashcards.data.repository.DataRepository
import com.antsfamily.danskflashcards.data.repository.DataRepositoryImpl
import com.antsfamily.danskflashcards.data.source.local.LocalSource
import com.antsfamily.danskflashcards.data.source.local.LocalSourceImpl
import com.antsfamily.danskflashcards.data.source.remote.RemoteSource
import com.antsfamily.danskflashcards.data.source.remote.RemoteSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindsDataRepository(dataRepositoryImpl: DataRepositoryImpl): DataRepository

    @Binds
    abstract fun bindsLocalSource(localSourceImpl: LocalSourceImpl): LocalSource

    @Binds
    abstract fun bindsRemoteSource(remoteSourceImpl: RemoteSourceImpl): RemoteSource
}
