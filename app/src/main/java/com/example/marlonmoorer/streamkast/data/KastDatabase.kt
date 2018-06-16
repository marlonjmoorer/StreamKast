package com.example.marlonmoorer.streamkast.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.InvalidationTracker
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.support.annotation.NonNull



@Database(entities = arrayOf(Subscription::class,Featured::class), version = 3)
abstract class KastDatabase:RoomDatabase() {
    abstract fun SubscriptionDao(): SubscriptionDao
    abstract fun FeaturedDao():FeaturedDao
    companion object {
        private var instance: KastDatabase? = null
        @Synchronized
        fun get(context: Context): KastDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context,
                        KastDatabase::class.java, "podcast.db")
                        .fallbackToDestructiveMigration()
                        .build()

            }
            return instance!!
        }


    }


}
