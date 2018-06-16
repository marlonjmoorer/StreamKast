package com.example.marlonmoorer.streamkast.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*


@Entity
class Subscription {


    @PrimaryKey()
    var podcastId:Int?=null


    var title:String?=null


    var thumbnail:String?=null

}

@Dao
interface SubscriptionDao {

    @get:Query("SELECT * FROM subscription")
    val all: LiveData<List<Subscription>>

    @Query("SELECT * FROM subscription  WHERE  podcastId = :id LIMIT 1")
    fun getById(id:String):Subscription

    @Query("SELECT EXISTS(SELECT 1  FROM subscription WHERE  podcastId = :id)")
    fun exist(id :String):Boolean

    @Insert
    fun insert(subscription:Subscription)

    @Insert
    fun insertAll(subscriptions: List<Subscription>)

    @Update
    fun update(subscription:Subscription)

    @Delete
    fun delete(subscription:Subscription)
}