package com.antsfamily.danskflashcards.data.di

import android.content.Context
import com.antsfamily.danskflashcards.data.source.remote.CacheInterceptor
import com.antsfamily.danskflashcards.data.FirebaseAuthInterceptor
import com.antsfamily.danskflashcards.data.source.remote.FirebaseHandler
import com.antsfamily.danskflashcards.data.source.remote.WordsApiService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideGson(): Gson = Gson()

    @Singleton
    @Provides
    fun provideCacheInterceptor(): CacheInterceptor = CacheInterceptor()

    @Singleton
    @Provides
    fun provideFirebaseAuthInterceptor(
        firebaseHandler: FirebaseHandler
    ): FirebaseAuthInterceptor = FirebaseAuthInterceptor(firebaseHandler)

    @Singleton
    @Provides
    fun provideCache(@ApplicationContext context: Context): Cache {
        val cacheDirectory = File(context.cacheDir, "flashcards_cache")
        return Cache(cacheDirectory, 5 * 1024 * 1024)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        cache: Cache,
        firebaseAuthInterceptor: FirebaseAuthInterceptor,
        cacheInterceptor: CacheInterceptor,
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(firebaseAuthInterceptor)
            .addNetworkInterceptor(cacheInterceptor)
            .build()
        return okHttpClient
    }

    @Singleton
    @Provides
    fun provideWordsApiService(
        gson: Gson,
        client: OkHttpClient
    ): WordsApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(WordsApiService.API_ENDPOINT)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(WordsApiService::class.java)
    }
}
