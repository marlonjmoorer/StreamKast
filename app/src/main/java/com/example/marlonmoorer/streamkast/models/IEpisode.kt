package com.example.marlonmoorer.streamkast.models

import com.example.marlonmoorer.streamkast.toTime
import java.io.Serializable

interface IEpisode:Serializable{
    var guid:String
    var url:String
    var title:String
    var author:String
    var thumbnail:String
    var description: String
    var duration:Int?
    val time
        get() = duration?.toTime()
}


