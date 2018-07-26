package com.example.marlonmoorer.streamkast.data

import android.arch.persistence.room.*
import com.example.marlonmoorer.streamkast.models.IEpisode

@Entity()
class SavedEpisode:IEpisode {
    @PrimaryKey()
    override var guid=""
    override var url: String=""
    override var thumbnail=""
    override var description=""
    override var author=""
    override var title=""
    override var duration:Int?=0
    var length=0
    var link=""
    var pupDate= ""
    var downloadId:Long=0
    var podcastId=""

}

