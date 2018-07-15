package com.example.marlonmoorer.streamkast.models

import android.graphics.Bitmap
import java.io.Serializable

data class EpisodeModel(
        var url:String?=null,
        var title:String?=null,
        var author:String?=null,
        var thumbnail:String?=null,
        var description: String?=null,
        var bitmapImage: Bitmap?=null
): Serializable