package com.edify.challenge.domain.stores

import com.edify.challenge.AppConstants

interface LocalDataStore {

    fun saveGifUri(gifUrl: String, fileName: String, folderName: String = AppConstants.FOLDER_NAME)
}