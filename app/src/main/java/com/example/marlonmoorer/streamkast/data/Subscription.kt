package com.example.marlonmoorer.streamkast.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*


@Entity
class Subscription {

    @PrimaryKey(autoGenerate = true)
    var id:Int?=null

    @ColumnInfo(name = "showId")
    var showId:Int?=null

    @ColumnInfo(name="title")
    var title:String?=null

    @ColumnInfo(name = "thumbnail")
    var thumbnail:String?=null

}

@Dao
interface SubscriptionDao {

    @get:Query("SELECT * FROM subscription")
    val all: LiveData<List<Subscription>>

    @Query("SELECT * FROM subscription  WHERE  showId = :showId LIMIT 1")
    fun getById(showId:String):Subscription

    @Query("SELECT EXISTS(SELECT 1  FROM subscription WHERE  showId = :showId)")
    fun exist(showId:String):Boolean

    @Insert
    fun insert(subscription:Subscription)

    @Insert
    fun insertAll(subscriptions: List<Subscription>)

    @Update
    fun update(subscription:Subscription)

    @Delete
    fun delete(subscription:Subscription)
}