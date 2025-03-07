package com.antsfamily.danskflashcards.data.di

import com.antsfamily.danskflashcards.data.repository.FirestoreDataRepository
import com.antsfamily.danskflashcards.data.repository.FirestoreDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindsDataRepository(dataRepositoryImpl: FirestoreDataRepositoryImpl): FirestoreDataRepository
}
