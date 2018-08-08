package com.marlonmoorer.streamkast.data

import android.arch.persistence.room.*


@Entity
class Subscription {


    @PrimaryKey()
    var podcastId:Int?=null


    var title:String?=null


    var thumbnail:String?=null

}

