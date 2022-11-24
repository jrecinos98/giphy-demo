package com.edify.challenge.di

import com.edify.challenge.App
import com.edify.challenge.domain.stores.LocalDataStore
import com.edify.challenge.storage.LocalDataStoreImpl
import dagger.Module
import dagger.Provides


@Module
class StorageModule(private val application: App) {

    @Provides
    fun providesLocalDataStore() : LocalDataStore {
        return LocalDataStoreImpl(application)
    }
}