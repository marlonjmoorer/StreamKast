package com.example.marlonmoorer.streamkast

import android.net.Uri
import com.example.marlonmoorer.streamkast.api.ItunesRepository
import com.github.magneticflux.rss.createRssPersister
import com.github.magneticflux.rss.namespaces.standard.elements.Rss
import org.junit.Test

import org.junit.Assert.*
import java.net.URL

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val i= ItunesRepository()
        var cast=i.topPodCast()
        cast?.forEach {
            var feed=i.getPodcastById(it.Id!!)!!.feedUrl!!
            var xml=URL(feed).readText()
            val persister = createRssPersister()
            val rssFeed = persister.read(Rss::class.java, xml)
            print("hi")
        }


       // ItunesRepository().parseFeed("https://www.npr.org/rss/podcast.php?id=510298")
        assertEquals(4, 2 + 2)
    }
}
