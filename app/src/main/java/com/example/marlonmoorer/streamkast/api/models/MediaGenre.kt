package com.example.marlonmoorer.streamkast.api.models

import android.util.Log

/**
 * Created by marlonmoorer on 3/22/18.
 */
enum class MediaGenre(private val value:String){


    Arts("1301"){
        override fun displayname()="Art"
        //override fun imageResource(): Int? = R.drawable.icons8_art_prices_filled
    },
    Comedy("1303"){

        override fun displayname()="Comedy"
       // override fun imageResource(): Int? = R.drawable.icons8_theatre_mask_filled
    },
    Education("1304"){
        override fun displayname()="Education"
       // override fun imageResource(): Int? = R.drawable.icons8_book_filled

    },
    Family("1305"){

        override fun displayname()="Kids & Family"
       // override fun imageResource(): Int? =R.drawable.icons8_family
    },
    Health("1307"){
        override fun displayname()="Health"
       // override fun imageResource(): Int? = R.drawable.icons8_heart_health_filled
    },
    TV("1309"){
        override fun displayname()="TV & Film"
       // override fun imageResource(): Int? = R.drawable.icons8_clapperboard_filled
    },
    Music("1310"){
        override fun displayname()="Music"
       // override fun imageResource(): Int? = R.drawable.icons8_music
    },
    News("1311"){
        override fun displayname()="News & Politics"
      //  override fun imageResource(): Int? = R.drawable.icons8_news_filled
    },
    Religion("1314"){
        override fun displayname()="Religion & Spirituality"
      //  override fun imageResource(): Int? = R.drawable.icons8_orthodox_church_filled
    },
    Science("1315"){
        override fun displayname()="Science & Medicine"
      //  override fun imageResource(): Int? = R.drawable.icons8_atom_editor_filled
    },
    Sports("1316"){
        override fun displayname()="Sports & Recreation"
       // override fun imageResource(): Int? = R.drawable.icons8_basketball_filled
    },
    Technology("1318"){
        override fun displayname()="Technology"
      //  override fun imageResource(): Int? = R.drawable.icons8_computer_filled
    },
    Business("1321"){
        override fun displayname()="Business"
      //  override fun imageResource(): Int? = R.drawable.icons8_business_filled
    },
    Games("1323"){
        override fun displayname()="Games & Hobbies"
      //  override fun imageResource(): Int? = R.drawable.icons8_controller_filled
    },
    Culture("1324"){
        override fun displayname()="Society & Culture"
       // override fun imageResource(): Int? = R.drawable.icons8_globe_filled
    },
    Government("1325"){
        override fun displayname() ="Government & Organizations"
       // override fun imageResource(): Int? = R.drawable.icons8_us_capitol_filled
    };

    abstract fun displayname(): String

    val id :String
        get() = this.value

    companion object {
        private val map= MediaGenre.values().associateBy{it.id}

        fun parse(value:String):MediaGenre?{
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
