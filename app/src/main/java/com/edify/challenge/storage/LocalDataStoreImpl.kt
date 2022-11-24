package com.edify.challenge.storage

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.edify.challenge.AppConstants
import com.edify.challenge.domain.stores.LocalDataStore
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import javax.inject.Inject

class LocalDataStoreImpl @Inject constructor(
    private val context: Context
) : LocalDataStore {
    override fun saveGifUri(gifUrl: String, fileName: String, folderName: String) {
        val uri = createGifUri(fileName, folderName )
        try{
            context.contentResolver.openOutputStream(uri).use {
                val gifBuffer = Glide.with(context).asGif().load(gifUrl).submit().get().buffer
                val bytes = ByteArray(gifBuffer.capacity())
                (gifBuffer.clear() as ByteBuffer).get(bytes)    //Both ".clear()" and "as ByteBuffer" are mandatory!!
                it?.write(bytes)
            }
        }catch (t : Throwable){
            File(uri.path).delete()
            throw t
        }

    }

    private fun createGifUri(fileName: String, folderName: String): Uri {
        val uri : Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)             //file name
                put(MediaStore.MediaColumns.MIME_TYPE, AppConstants.MEDIA_EXT)             //file type
                put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DCIM}/$folderName")     //file location
            }
            uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
        }
        else{
            val directory: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + File.separator + folderName)!!
            if (!directory.exists()){
                directory.mkdirs()
            }
            uri = File(directory, "$fileName.gif").toUri()
        }
        return uri
    }
}