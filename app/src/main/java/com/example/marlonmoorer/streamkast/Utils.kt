package com.example.marlonmoorer.streamkast

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.util.Xml
import com.example.marlonmoorer.streamkast.api.models.Channel
import com.example.marlonmoorer.streamkast.api.models.Episode
import org.w3c.dom.*
import java.io.InputStream
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.os.Looper



import java.io.File

import java.io.BufferedWriter
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*
import org.joda.time.format.PeriodFormatterBuilder
import org.joda.time.format.PeriodFormatter
import java.time.Duration


class Utils{




    class AppExceptionHandler(private val context:Context):Thread.UncaughtExceptionHandler{

        private val ERROR_FILE = AppExceptionHandler::class.java.getSimpleName() + ".error"
        var handler:Thread.UncaughtExceptionHandler
        init {
            handler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(this)
        }

        override fun uncaughtException(thread: Thread?, throwable: Throwable?) {
            Log.e("parser Error",throwable?.message)
            throwable?.printStackTrace()
            val file = File(context.filesDir, ERROR_FILE)
            // log this exception ...
            val buf = BufferedWriter(FileWriter(file, true))
            buf.append(throwable?.message)
            buf.newLine()
            buf.append(throwable?.stackTrace.toString())
            buf.close()

            Thread({
                Looper.prepare()
                val alert=AlertDialog.Builder(context).create()
                alert.setMessage(throwable?.message)
                alert.show()
                Looper.loop()
            }).start()

            try {
                Thread.sleep(4000) // Let the Toast display before app will get shutdown
            } catch (e: InterruptedException) {
                // Ignored.
            }

           System.exit(1)

        }
    }
    companion object {
        var exceptionHandler: ((Throwable) -> Unit)= {throwable ->
            throw  throwable
        }
        fun parse(inputStream: InputStream):Channel?{


            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()

            parser.setInput(inputStream,null)
            var eventType = parser.eventType
            var channel:Channel?=null
            while (eventType != XmlPullParser.END_DOCUMENT) {
                try {
                    when(eventType){
                        XmlPullParser.START_TAG->{
                            val name= parser.name
                            when(name){
                                "channel"->{
                                    channel= parseChannel(parser)
                                    return  channel
                                }
                            }
                        }
                    }
                    eventType = parser.next()
                }catch (ex:Exception){
                    ex.printStackTrace()
                    throw ex
                }

            }
            return  channel
        }
        fun parseChannel(parser: XmlPullParser):Channel{
            parser.require(XmlPullParser.START_TAG,null,"channel")
            val channel= Channel()
            while (!(parser.eventType==XmlPullParser.END_TAG && parser.name=="channel")){
                val event= parser.eventType
                when(event){
                    XmlPullParser.START_TAG->{
                        val name=parser.name
                        when(name){
                            "title"-> channel.title =parser.nextText()
                            "link"->channel.link=parser.nextText()
                            "description"-> channel.description=parser.nextText()
                            "author"->channel.author=parser.nextText()
                            "image"->{
                                val prefix= parser.prefix
                                if(prefix==null){
                                    val url=parseImage(parser)
                                    channel.thumbnail=url
                                }
                            }
                            "category"-> {
                                val prefix= parser.prefix
                                if(prefix=="itunes"){
                                 channel.categories.add(parser.getAttributeValue(null,"text"))
                                }
                            }
                            "item"-> {
                                val episode= parseItem(parser)

                                episode?.let {
                                    if(episode.thumbnail.isEmpty()){
                                        episode.thumbnail=channel.thumbnail
                                    }
                                    if(episode.author.isEmpty()){
                                        episode.author=channel.author
                                    }
                                    channel.episodes.add(episode)
                                }
                            }

                        }
                    }


                }
                parser.next()

            }
            return  channel

        }
        fun parseItem(parser: XmlPullParser):Episode?{
            parser.require(XmlPullParser.START_TAG,null,"item")
            val episode=Episode().apply{
                while (!(parser.eventType==XmlPullParser.END_TAG && parser.name=="item")){
                    val event= parser.eventType
                    when(event){
                        XmlPullParser.START_TAG->{
                            val name=parser.name
                            when(name){
                                "title"-> title= parser.nextText()
                                "guid"-> guid= parser.nextText()
                                "author"-> author= parser.nextText()
                                "image"->{
                                    if(parser.prefix=="itunes"){
                                        thumbnail= parser.nextText()
                                    }
                                }
                                "enclosure"->{
                                    url= parseAttributeOrNull(parser,"url")
                                    length=  parseAttributeOrNull(parser,"length")
                                }
                                "description"-> description=parser.nextText()
                                "duration"->{
                                    var text=parser.nextText()
                                    var format:String?=null
                                    val formatter = PeriodFormatterBuilder()


                                    System.out.println(duration)
                                    val colonCount= text.split(':').size
                                    if(text==null){}
                                    else if(colonCount==2){
                                        formatter.appendMinutes().appendLiteral(":").appendSeconds()
                                    }else if(colonCount==3){
                                        formatter.appendHours()
                                                .appendLiteral(":")
                                                .appendMinutes()
                                                .appendLiteral(":")
                                                .appendSeconds()
                                    }else{
                                        duration= text.toInt()*1000
                                    }
                                    if(duration==0){
                                        var d=formatter
                                                .toFormatter()
                                                .parsePeriod(text)
                                                .toStandardDuration()
                                        duration=      d.millis.toInt()
                                    }

                                }
                            }
                        }
                    }
                    parser.next()
                }
            }
            return episode
        }
        fun parseImage(parser: XmlPullParser): String {
            parser.require(XmlPullParser.START_TAG,null,"image")
            while (!(parser.eventType==XmlPullParser.END_TAG && parser.name=="image")){
                val event= parser.eventType
                when(event){
                    XmlPullParser.START_TAG->{
                        val name=parser.name
                        when(name){
                            "url"-> return  parser.nextText()
                        }
                    }
                }
                parser.next()
            }
            return ""
        }
        fun  parseAttributeOrNull(parser: XmlPullParser,attrName:String):String{
            try {
                return parser.getAttributeValue(null,attrName)
            }
            catch (ex:Exception){
                return ""
            }
        }
    }
}
