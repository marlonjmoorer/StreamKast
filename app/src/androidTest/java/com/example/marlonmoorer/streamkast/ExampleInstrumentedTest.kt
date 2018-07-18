package com.example.marlonmoorer.streamkast

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.example.marlonmoorer.streamkast", appContext.packageName)

        arrayOf(
                "http://joeroganexp.joerogan.libsynpro.com/rss"
                //"https://feeds.megaphone.fm/stuffyoushouldknow" //,
                //  "https://rss.art19.com/id10t",
                //  "https://www.npr.org/rss/podcast.php?id=381444908",
                //  "https://www.npr.org/rss/podcast.php?id=510289"
        ).forEach {

            var dom=""
            var pp=""
            var request = Request.Builder()
                    .url(it)
                    .build()
            var httpClient= OkHttpClient()

            var response = httpClient.newCall(request).execute()
            response.body()?.byteStream().use {
                val startTime = System.currentTimeMillis()
                Utils.parseFeed(it!!)
                val endTime = System.currentTimeMillis()
                dom="Dom  took " + (endTime - startTime) + " milliseconds"
            }

            var response2 = httpClient.newCall(request).execute()
            response2.body()?.byteStream().use {
                val startTime = System.currentTimeMillis()
                Utils.pullParse(it!!)
                val endTime = System.currentTimeMillis()
                pp="PullParser  took " + (endTime - startTime) + " milliseconds"
            }







            print("Done")
        }
    }
}
