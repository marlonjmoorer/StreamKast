package com.marlonmoorer.streamkast

import com.marlonmoorer.streamkast.api.Repository
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Test

import org.junit.Assert.*
import org.w3c.dom.NodeList
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

        arrayOf(
                "https://feeds.megaphone.fm/stuffyoushouldknow" //,
              //  "https://rss.art19.com/id10t",
              //  "https://www.npr.org/rss/podcast.php?id=381444908",
              //  "https://www.npr.org/rss/podcast.php?id=510289"
        ).forEach {
            val request = Request.Builder()
                    .url(it)
                    .build()
            val httpClient= OkHttpClient()
            val response = httpClient.newCall(request).execute()
            val result= response.body()?.byteStream()

            val startTime = System.currentTimeMillis()

            val endTime = System.currentTimeMillis()
            var dom="Sax took " + (endTime - startTime) + " milliseconds"


            print("Done")
        }

        val url = "http://joeroganexp.joerogan.libsynpro.com/rss"
//        val url = "http://joeroganexp.joerogan.libsynpro.com/rss"
//        val doc = URL(url).asXmlDoc()
//        val rss=doc["rss"].getEelement("channel")
//        val items=rss.getList("item")
//        var sum= rss["itunes:summary"]
//        items.forEach<Element> {
//            val title= it["title"]
//            val b= it.has("junk")
//            print(title)
//
//        }
//        assertEquals(4, 2 + 2)


    }


//    val NodeList.Nodes
//        get() = this.


}
