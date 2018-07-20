package com.example.marlonmoorer.streamkast.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import java.util.*

@Dao
interface HistoryDao {

    @get:Query("SELECT * FROM playbackhistory ORDER BY lastPlayed DESC")
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
    fun getMostRecent(): LiveData<PlaybackHistory>

    @Query("UPDATE playbackhistory SET lastPlayed = :date WHERE guid= :id ")
    fun uppdateLastPlayed(date: Date, id:String)
}