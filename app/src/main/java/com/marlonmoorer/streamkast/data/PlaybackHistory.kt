package com.marlonmoorer.streamkast.data

import android.arch.persistence.room.*
import com.marlonmoorer.streamkast.models.IEpisode
import com.marlonmoorer.streamkast.toDateString
import java.util.*


@Entity()
class PlaybackHistory:IEpisode {
    @PrimaryKey()
    override var guid=""
    override var url=""
    override var title=""
    override var author:String=""
    override var thumbnail:String=""
    override var description: String=""
    override var duration:Int?=0
    @TypeConverters(Converters::class)
    var lastPlayed:Date?=null

}

