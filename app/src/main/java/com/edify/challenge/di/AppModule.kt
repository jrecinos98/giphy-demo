package com.edify.challenge.di

import android.app.Application
import com.edify.challenge.App
import com.edify.challenge.domain.GifRepository
import com.edify.challenge.domain.stores.LocalDataStore
import com.edify.challenge.features.GifRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: App){

    @Provides
    @Singleton
    fun getApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    fun providesRepository(localDataStore: LocalDataStore) : GifRepository = GifRepositoryImpl(localDataStore)
}
