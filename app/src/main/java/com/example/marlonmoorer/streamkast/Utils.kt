package com.example.marlonmoorer.streamkast

import android.util.Xml
import com.example.marlonmoorer.streamkast.api.models.Channel
import com.example.marlonmoorer.streamkast.api.models.Episode
import org.w3c.dom.*
import java.io.InputStream
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory




class Utils{
    companion object {
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
                        XmlPullParser.START_DOCUMENT-> {}
                        XmlPullParser.START_TAG->{
                            val name= parser.name
                            when(name){
                                "channel"->{
                                    channel= parseChannel(parser)
                                    return  channel
                                }
                            }
                        }
                        XmlPullParser.END_TAG->{}

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
            val ch= Channel()
            while (!(parser.eventType==XmlPullParser.END_TAG && parser.name=="channel")){
                val event= parser.eventType
                when(event){
                    XmlPullParser.START_TAG->{
                        val name=parser.name
                        when(name){
                            "title"-> ch.title =parser.nextText()
                            "link"->ch.link=parser.nextText()
                            "description"-> ch.description=parser.nextText()
                            "itunes:author"->ch.author=parser.nextText()
                            "image"->{
                                val prefix= parser.prefix
                                if(prefix==null){
                                    val url=parseImage(parser)
                                    ch.thumbnail=url
                                }
                            }
                            "category"-> {
                                val prefix= parser.prefix
                                if(prefix=="itunes"){
                                 ch.categories.add(parser.getAttributeValue(null,"text"))
                                }
                            }
                            "item"-> {
                                val ep= parseItem(parser)
                                if(ep?.thumbnail==""){
                                    ep?.thumbnail=ch.thumbnail
                                }
                                ep?.let { ch.episodes.add(it) }
                            }

                        }
                    }


                }
                parser.next()

            }
            return  ch

        }
        fun parseItem(parser: XmlPullParser):Episode?{
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
                                    mediaUrl= parser.getAttributeValue(null,"url")
                                    length= parser.getAttributeValue(null,"length")
                                }
                                "description"-> description=parser.nextText()
                            }
                        }
                    }
                    parser.next()
                }
            }
            return episode
        }
        fun parseImage(parser: XmlPullParser): String {
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
    }
}
