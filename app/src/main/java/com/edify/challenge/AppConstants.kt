package com.edify.challenge

import com.giphy.sdk.core.models.enums.MediaType
import com.giphy.sdk.core.models.enums.RatingType
import com.giphy.sdk.core.models.enums.RenditionType
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.drawables.ImageFormat
import com.giphy.sdk.ui.pagination.GPHContent
import com.giphy.sdk.ui.themes.GPHTheme
import com.giphy.sdk.ui.themes.GridType
import com.giphy.sdk.ui.views.GiphyGridView

object AppConstants {

    const val FOLDER_NAME =  "Edify"
    const val MEDIA_EXT = "image/gif"

    object GiphyConfig {
        var mediaType = MediaType.gif
        var fixedSizeCells = false
    }
}