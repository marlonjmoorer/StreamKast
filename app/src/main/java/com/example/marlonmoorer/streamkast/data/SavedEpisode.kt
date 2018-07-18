package com.example.marlonmoorer.streamkast.data

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

