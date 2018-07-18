package com.example.marlonmoorer.streamkast.data

import android.arch.persistence.room.*
import java.util.*


@Entity()
class PlaybackHistory {
    @PrimaryKey()
    var guid=""
    var url=""
    var title=""
    var author:String?=null
    var thumbnail:String?=null
    var description: String?=null
    var duration:Int=0
    @TypeConverters(Converters::class)
    var lastPlayed:Date?=null
}

