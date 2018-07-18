package com.example.marlonmoorer.streamkast

import android.app.Application
import android.util.Log
import com.example.marlonmoorer.streamkast.api.models.rss.Channel
import com.example.marlonmoorer.streamkast.api.models.rss.Episode
import com.example.marlonmoorer.streamkast.di.AppComponent
import com.example.marlonmoorer.streamkast.di.AppModule
import com.example.marlonmoorer.streamkast.di.DaggerAppComponent
import org.w3c.dom.*
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors
import java.util.stream.BaseStream

class Utils{
    companion object {
       fun parseFeed(stream: InputStream):Channel{
           val channel:Channel
           val doc = stream.toDocument()
           val channelElement=doc["rss"]["channel"]
           channel=Channel().apply {
               channelElement.childNodes.forEach {node->
                   if(node.nodeType== Node.ELEMENT_NODE && node is  Element){
                       when(node.nodeName){
                           "title"-> title=node.textContent
                           "link"->link=node.textContent
                           "description"-> description=node.textContent
                           "itunes:author"->author=node.textContent
                           "image"-> node.childNodes.forEach {n->
                               if(n.nodeName=="url"){
                                   thumbnail=n.textContent
                               }
                           }
                          // "itunes:image"-> thumbnail=node.getAttribute("href")
                           "itunes:category"-> categories.add(node.getAttribute("text"))
                           "item"-> {
                               val ep=parseEpisode(node)
                               if(ep.thumbnail==""){
                                   ep.thumbnail=thumbnail
                               }
                               episodes.add(ep)
                           }
                       }
                   }
               }
           }

          return channel
       }
        fun parseEpisode(node:Element):Episode{
              return Episode().apply {
                    node.childNodes.forEach {node->
                        if(node.nodeType== Node.ELEMENT_NODE && node is  Element){
                        when(node.nodeName){
                            "title"-> title=node.textContent
                            "guid"-> guid= node.textContent
                            "pubDate"->publishedDate= node.textContent.toDate()
                            "itunes:author"-> node.textContent
                            "itunes:duration"-> duration = when{
                                node.textContent.contains(":")-> node.textContent
                                else ->(node.textContent.toInt() *1000).toTime()
                            }
                            "itunes:image"-> thumbnail=node.getAttribute("href")
                            "enclosure"->{
                                mediaUrl= node.getAttribute("url")
                                length= node.getAttribute("length")
                            }
                            "description"-> description=node.textContent
                        }}
                    }
              }
        }
    }
}
