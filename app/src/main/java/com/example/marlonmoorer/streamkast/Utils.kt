package com.example.marlonmoorer.streamkast

import android.app.Application
import android.util.Log
import com.example.marlonmoorer.streamkast.api.models.rss.Channel
import com.example.marlonmoorer.streamkast.api.models.rss.Episode
import com.example.marlonmoorer.streamkast.di.AppComponent
import com.example.marlonmoorer.streamkast.di.AppModule
import com.example.marlonmoorer.streamkast.di.DaggerAppComponent
import org.w3c.dom.Element
import java.net.URL
import java.util.concurrent.Executors


private val BACKGROUND_THREAD= Executors.newSingleThreadExecutor()


fun async(fn:()->Unit){
    val future = BACKGROUND_THREAD.submit(fn)
    try {
        future.get()
    }catch (ex:Exception){
        Log.e("ERR",ex.message)
    }
}

class Utils{
    companion object {
       fun parseFeed(url:String):Channel{
           var channel:Channel?= null
           val doc = URL(url).asXmlDoc()
           val channelElement=doc["rss"]["channel"]
           channelElement.let{el->
               channel=Channel().apply {
                   title=el.text("title")
                   link=el.text("link")
                   description=el.text("description")
                   author=el.text("itunes:author")
                   thumbnail= el["itunes:image"].getAttribute("href")
                   category=el.getList("itunes:category").map {c->
                        c.getAttribute("text")
                   }
               }
           }
           channel!!.episodes= channelElement.getList("item").map{el->
               val time= el.text("itunes:duration")
               Episode().apply {
                   title=el.text("title")
                   guid=el.text("guid")
                   publishedDate=el.text("pubDate").toDate()
                   duration= if(time.matches("^\\d+:\\d{2}:\\d{2}\$".toRegex())) time else (time.toInt()*1000).toTime()
                   thumbnail=if (el.has("itunes:image")) el["itunes:image"].getAttribute("href")
                            else channel!!.thumbnail
                   el["enclosure"].let{enclosure->
                       mediaUrl = enclosure.getAttribute("url")
                       length=enclosure.getAttribute("length")
                   }
               }
           }
          return channel!!
       }
    }
}
