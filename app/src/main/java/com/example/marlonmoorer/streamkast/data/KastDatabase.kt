package com.example.marlonmoorer.streamkast.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.InvalidationTracker
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.support.annotation.NonNull



@Database(entities = arrayOf(Subscription::class), version = 2)
abstract class KastDatabase:RoomDatabase() {
    abstract fun SubscriptionDao(): SubscriptionDao

    companion object {
        private var instance: KastDatabase? = null
        @Synchronized
        fun get(context: Context): KastDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context,
                        KastDatabase::class.java, "podcast.db")
                        .fallbackToDestructiveMigration()
                        .build()
                instance?.getInvalidationTracker()?.addObserver(DataSourceTableObserver("subscription"))

            }
            return instance!!
        }


    }

   class DataSourceTableObserver(tableName: String) : InvalidationTracker.Observer(tableName) {

        //private var dataSource: DataSource? = null

        override fun onInvalidated(tables: Set<String>) {
           var f=4
        }

//        fun setCurrentDataSource(source: DataSource) {
//            dataSource = source
//        }

    }
}
