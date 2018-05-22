package com.example.marlonmoorer.streamkast

import com.example.marlonmoorer.streamkast.api.ItunesRepository
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
        ItunesRepository().topPodCast()
       // ItunesRepository().ParseFeed("https://www.npr.org/rss/podcast.php?id=510298")
        assertEquals(4, 2 + 2)
    }
}
