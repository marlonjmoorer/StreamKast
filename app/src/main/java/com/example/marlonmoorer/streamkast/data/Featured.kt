package com.example.marlonmoorer.streamkast.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.example.marlonmoorer.streamkast.api.models.chart.Field
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(primaryKeys = arrayOf("podcastId","genreId"))
class Featured {
    var podcastId=""
    var name=""
    var imageUrl=""
    var summary=""
    var author=""
   // var lastUpdateDate= Date()
    var genreId=""
}

@Dao
interface FeaturedDao {

    @get:Query("SELECT * FROM featured")
    val all: LiveData<List<Featured>>



    @Query("SELECT * FROM featured  WHERE  genreId = :id")
    fun getByGenreId(id:String):LiveData<List<Featured>>

    @Query("SELECT EXISTS(SELECT * FROM featured  WHERE  genreId = :id)")
    fun hasRows(id:String):Boolean

    @Insert
    fun insert(featured: Featured)

    @Insert
    fun insertAll(featuredShows: List<Featured>)

    @Update
    fun update(featured: Featured)

    @Delete
    fun delete(featured: Featured)
}
