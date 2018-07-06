package com.example.marlonmoorer.streamkast.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Entity()
class SavedEpisode {

    @PrimaryKey()
    var guid=""
    var podcastId=""
    var imageUrl=""
    var summary=""
    var author=""
    var pupDate= ""
    var title=""
    var duration=0
    var length=0
    var link=""
}

@Dao
interface EpisodeDao {

    @get:Query("SELECT * FROM savedepisode")
    val all: LiveData<List<SavedEpisode>>


    @Query("SELECT * FROM  savedepisode  WHERE  guid=:id")
    fun getById(id:String): LiveData<SavedEpisode>

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