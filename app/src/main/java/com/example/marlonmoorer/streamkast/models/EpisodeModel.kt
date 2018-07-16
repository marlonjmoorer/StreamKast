package com.example.marlonmoorer.streamkast.models


import java.io.Serializable

data class EpisodeModel(
        var guid:String="",
        var url:String?=null,
        var title:String?=null,
        var author:String?=null,
        var thumbnail:String?=null,
        var description: String?=null,
        var duration:Int=0,
        var autoPlay:Boolean=false
): Serializable