package com.example.marlonmoorer.streamkast.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface FeaturedDao {

    @get:Query("SELECT * FROM featured")
    val all: LiveData<List<Featured>>



    @Query("SELECT * FROM featured  WHERE  genreId = :id")
    fun getByGenreId(id:String): LiveData<List<Featured>>

    @Query("SELECT EXISTS(SELECT * FROM featured  WHERE  genreId = :id)")
    fun hasRows(id:String):Boolean

    @Query("DELETE from featured   WHERE  genreId = :genreId")
    fun clearGenreItems(genreId:String)

    @Insert
    fun insert(featured: Featured)

    @Insert
    fun insertAll(featuredShows: List<Featured>)

    @Update
    fun update(featured: Featured)

    @Delete
    fun delete(featured: Featured)
}