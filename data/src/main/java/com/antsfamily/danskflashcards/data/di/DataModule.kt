package com.antsfamily.danskflashcards.data.di

import com.antsfamily.danskflashcards.data.remote.FirestoreHandler
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

    @Singleton
    @Provides
    fun provideFirestoreHandler(firestore: FirebaseFirestore): FirestoreHandler =
        FirestoreHandler(firestore)


    @Singleton
    @Provides
    fun provideGson(): Gson = Gson()
}