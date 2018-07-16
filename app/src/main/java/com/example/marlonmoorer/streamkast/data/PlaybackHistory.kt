package com.example.marlonmoorer.streamkast.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import java.io.Serializable




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

    @Query("SELECT * FROM playbackhistory ORDER BY guid DESC LIMIT 1")
    fun getMostRecent():LiveData<PlaybackHistory>
}