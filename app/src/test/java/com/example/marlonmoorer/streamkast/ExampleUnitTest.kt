package com.example.marlonmoorer.streamkast

import com.example.marlonmoorer.streamkast.api.Repository
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val i= Repository()
        var cast=i.topPodCast()
        cast?.forEach {
            var feed=i.getPodcastById(it.Id!!)!!.feedUrl!!
            val t=i.parseService.parseFeed(feed).execute().body()
            print("hi")
        }


       // Repository().parseFeed("https://www.npr.org/rss/podcast.php?id=510298")
        assertEquals(4, 2 + 2)
    }
}
