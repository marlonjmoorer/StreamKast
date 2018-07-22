package com.example.marlonmoorer.streamkast.models

import com.example.marlonmoorer.streamkast.data.SavedEpisode
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

    fun < T :IEpisode> fromEpisode(episode: IEpisode):T{
        guid= episode.guid
        title=episode.title
        author=episode.author
        thumbnail=episode.thumbnail
        description=episode.description
        duration=episode.duration
        url=episode.url
        return this as T
    }
}


