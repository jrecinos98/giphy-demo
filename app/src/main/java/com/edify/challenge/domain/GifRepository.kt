package com.edify.challenge.domain

import com.edify.challenge.AppConstants

interface GifRepository {

    suspend fun saveGif(gifUrl : String, fileName: String, folderName: String = AppConstants.FOLDER_NAME)

}