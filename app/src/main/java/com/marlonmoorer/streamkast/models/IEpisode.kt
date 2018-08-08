package com.marlonmoorer.streamkast.models

import com.marlonmoorer.streamkast.data.SavedEpisode
import com.marlonmoorer.streamkast.toTime
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

    companion object {
        inline  fun < reified T :IEpisode>  fromEpisode(episode: IEpisode):T{
            val ep= T::class.java.newInstance().apply {
                guid= episode.guid
                title=episode.title
                author=episode.author
                thumbnail=episode.thumbnail
                description=episode.description
                duration=episode.duration
                url=episode.url
            }
            return  ep
        }
    }
}




