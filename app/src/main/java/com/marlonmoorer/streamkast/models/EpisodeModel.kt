package com.marlonmoorer.streamkast.models


import java.io.Serializable

data class EpisodeModel(
        override var guid:String="",
        override var url:String="",
        override var title:String="",
        override var author:String="",
        override var thumbnail:String="",
        override var description: String="",
        override var duration:Int?=0,
        var autoPlay:Boolean=false
): Serializable,IEpisode