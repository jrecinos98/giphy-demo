package com.edify.challenge.features

import com.edify.challenge.domain.GifRepository
import com.edify.challenge.domain.stores.LocalDataStore

class GifRepositoryImpl(
    private val localDataStore: LocalDataStore
) : GifRepository {

    override suspend fun saveGif(gifUrl: String, fileName: String, folderName: String) {
        localDataStore.saveGifUri(gifUrl, fileName, folderName )
    }
}