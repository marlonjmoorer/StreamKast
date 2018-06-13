package com.example.marlonmoorer.streamkast.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(Subscription::class), version = 1)
abstract class KastDatabase:RoomDatabase() {
    abstract fun SubscriptionDao(): SubscriptionDao

    companion object {
        private var instance: KastDatabase? = null
        @Synchronized
        fun get(context: Context): KastDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context,
                        KastDatabase::class.java, "podcast.db").build()
            }
            return instance!!
        }


    }
}
