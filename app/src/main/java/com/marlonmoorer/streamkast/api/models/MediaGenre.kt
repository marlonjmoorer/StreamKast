package com.marlonmoorer.streamkast.api.models

import android.util.Log
import com.marlonmoorer.streamkast.R

/**
 * Created by marlonmoorer on 3/22/18.
 */
enum class MediaGenre(private val value:String){

    Featured("0"),

    Arts("1301"){
        override var displayname="Art"
        override var imageId = R.drawable.icons8_art
    },
    Comedy("1303"){

        override var displayname="Comedy"
        override var imageId=R.drawable.icons8_comedy
    },
    Education("1304"){
        override var displayname="Education"
        override var imageId=R.drawable.icons8_education

    },
    Family("1305"){

        override var displayname="Kids & Family"
        override var imageId=R.drawable.icons8_family
    },
    Health("1307"){
        override var displayname="Health"
        override var imageId=R.drawable.icons8_heart_health
    },
    TV("1309"){
        override var displayname="TV & Film"
        override var imageId=R.drawable.icons8_film_slate
    },
    Music("1310"){
        override var displayname="Music"
        override var imageId=R.drawable.icons8_music_note_outline
    },
    News("1311"){
        override var displayname="News & Politics"
        override var imageId=R.drawable.icons8_news
    },
    Religion("1314"){
        override var displayname="Religion & Spirituality"
        override var imageId=R.drawable.icons8_praying_symbol
    },
    Science("1315"){
        override var displayname="Science & Medicine"
        override var imageId=R.drawable.icons8_science_application
    },
    Sports("1316"){
        override var displayname="Sports & Recreation"
        override var imageId=R.drawable.icons8_college_sports
    },
    Technology("1318"){
        override var displayname="Technology"
        override var imageId=R.drawable.icons8_technology
    },
    Business("1321"){
        override var displayname="Business"
        override var imageId=R.drawable.icons8_business
    },
    Games("1323"){
        override var displayname="Games & Hobbies"
        override var imageId=R.drawable.icons8_video_game_controller_outline
    },
    Culture("1324"){
        override var displayname="Society & Culture"
        override var imageId=R.drawable.icons8_ankh
    },
    Government("1325"){
        override var displayname ="Government & Organizations"
        override var imageId=R.drawable.icons8_public_record_keeping
    };

    open var displayname: String=""
    open  var imageId: Int=0
    val id :String
        get() = this.value

    companion object {
        val categories=MediaGenre.values().filter {it.id!="0"}
        private val map= categories.associateBy{it.id}

        fun parse(value:String):MediaGenre?{
            if(value=="0") return Featured
            try {
                return map[value]
            }
            catch (ex:Exception){
                Log.e("",ex.message,ex)
                return null
            }
        }
    }
}
