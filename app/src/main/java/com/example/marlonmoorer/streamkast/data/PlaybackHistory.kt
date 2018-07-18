package com.example.marlonmoorer.streamkast.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import java.io.Serializable
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

@Dao
interface HistoryDao {

    @get:Query("SELECT * FROM playbackhistory")
    val all: LiveData<List<PlaybackHistory>>

    @Query("SELECT * FROM  playbackhistory WHERE  guid=:id")
    fun getById(id:String): LiveData<PlaybackHistory>

    @Query("SELECT EXISTS(SELECT * FROM playbackhistory WHERE  guid = :id)")
    fun exist(id:String):Boolean

    @Query("DELETE from playbackhistory WHERE  guid = :id")
    fun removeFromHistory(id:String)

    @Insert
    fun insert(episode: PlaybackHistory)

    @Delete
    fun delete(episode: PlaybackHistory)

    @Query("SELECT * FROM playbackhistory ORDER BY lastPlayed DESC LIMIT 1")
    fun getMostRecent():LiveData<PlaybackHistory>

    @Query("UPDATE playbackhistory SET lastPlayed = :date WHERE guid= :id ")
    fun uppdateLastPlayed(date: Date,id:String)
}