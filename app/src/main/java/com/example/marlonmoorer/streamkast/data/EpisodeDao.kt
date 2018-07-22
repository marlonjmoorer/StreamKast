package com.example.marlonmoorer.streamkast.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface EpisodeDao {

    @get:Query("SELECT * FROM savedepisode")
    val all: List<SavedEpisode>

    @Query("SELECT * FROM savedepisode WHERE downloadId IN (:ids)")
    fun queryObjects(ids:List<Int>):SavedEpisode

    @Query("SELECT * FROM  savedepisode  WHERE  downloadId=:id")
    fun getByDownloadId(id:Int):SavedEpisode

    @Query("SELECT * FROM  savedepisode  WHERE  guid=:id")
    fun getById(id:String): SavedEpisode?

    @Query("SELECT EXISTS(SELECT * FROM savedepisode WHERE  guid = :id)")
    fun exist(id:String):Boolean

    @Query("DELETE from savedepisode  WHERE  guid = :id")
    fun removeEpisode(id:String)

    @Insert
    fun insert(episode: SavedEpisode)

    @Insert
    fun insertAll(episodes: List<SavedEpisode>)


    @Delete
    fun delete(episode: SavedEpisode)
}