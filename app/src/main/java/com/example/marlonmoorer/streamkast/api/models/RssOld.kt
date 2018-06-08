package com.example.marlonmoorer.streamkast.api.models.L



import org.simpleframework.xml.*
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


//@Root(name = "rss",strict = false)
//class Rss {
//
//    @field:Element(name = "channel")
//    var channel: Channel? = null
//
//}
//@Root(strict = false)
//class Channel {
//
//    @field:Element(name = "title")
//    var title: String? = null
//
//
//    @field:Element(name = "description")
//    var description: String? = null
//
//    @field:ElementList(name = "item")
//    var episodes: List<Episode>? = null
//
//
////    @set:Element(name = "link")
////    @get:Element(name = "link")
//    var link: String? = null
//
//
//
//    @field:Element(name = "itunes:author")
//    var author: String? = null
//
//    //@field:Element(name = "image")
//    var thumbnail: Image? = null
//
//
//
//}
//
//@Root(strict = false)
//class Episode : Serializable {
//
//
//
//    @field:Element(name = "pubDate")
//    var pubDate: String? = null
//
//    val publishedDate:String
//        get() {
//            val date= SimpleDateFormat("E, d MMM yyyy HH:mm:ss Z", Locale.US).parse(pubDate)
//            return SimpleDateFormat("EEE, MMM dd, yyyy", Locale.US).format(date)
//        }
//
//
//
//    @field:Element(name = "title")
//    var title: String? = null
//
//    @field:Element(name = "enclosure")
//    var enclosure: Enclosure? = null
//
////
////    @field:Element(name = "description")
//    var description: String? = null
//
//    @field:Element(name = "link")
//    var link: String? = null
//
//
//}
//@Root(strict = false)
//class Enclosure {
//
//    @field:Attribute(name = "url")
//    var url:String?=null
//
//    @field:Attribute(name = "length")
//    var length:Long?=null
//
//}
//@Root(strict = false)
//class Image{
//    @field:Element(name = "url")
//    var url:String?=null
//
//}




@Root(name = "rss",strict = false)
class Rss {
    @get:Element(name = "channel", required = false)
    @set:Element(name = "channel", required = false)
    var channel: Channel? = null

    @get:Attribute(name = "content", required = false)
    @set:Attribute(name = "content", required = false)
    var content: String? = null


   @Root(strict = false)
    class Channel {

        @get:Element(name = "link", required = false)
        @set:Element(name = "link", required = false)
        var link: String? = null

        @get:Element(name = "title", required = false)
        @set:Element(name = "title", required = false)
        var title: String? = null

        @get:Element(name = "pubDate", required = false)
        @set:Element(name = "pubDate", required = false)
        var pubDate: String? = null


        @get:Element(name = "lastBuildDate", required = false)
        @set:Element(name = "lastBuildDate", required = false)
        var lastBuildDate: String? = null


        @get:Element(name = "summary", required = false)
        @set:Element(name = "summary", required = false)
        var summary: String? = null

        // @get:Element(name = "image", required = false)
        // @set:Element(name = "image", required = false)

        @get:Element(required = false)
        @set:Element(required = false)
        @Namespace(prefix = "itunes", reference = "http://www.itunes.com/dtds/podcast-1.0.dtd")
        var image: Image? = null


        @get:Element(name = "author", required = false)
        @set:Element(name = "author", required = false)
        var author: String? = null

        @get:Element(name = "keywords", required = false)
        @set:Element(name = "keywords", required = false)
        var keywords: String? = null

        @get:ElementList(name = "category", inline = true, required = false)
        @set:ElementList(name = "category", inline = true, required = false)
        var category: List<Category>? = null

        @get:Element(name = "explicit", required = false)
        @set:Element(name = "explicit", required = false)
        var explicit: String? = null

        @get:Element(name = "description", required = false)
        @set:Element(name = "description", required = false)
        var description: String? = null


        @get:ElementList(name = "item", inline = true, required = false)
        @set:ElementList(name = "item", inline = true, required = false)
        var episodes: List<Episode>? = null


    }

    class Link {

        @set:Attribute(name = "href", required = false)
        var href: String? = null


        @set:Attribute(name = "rel", required = false)
        var rel: String? = null


        @set:Attribute(name = "type", required = false)
        var type: String? = null


    }

    class Image {

        @get:Element(name = "url", required = false)
        @set:Element(name = "url", required = false)
        var url: String? = null

        @get:Element(name = "title", required = false)
        @set:Element(name = "title", required = false)
        var title: String? = null

        @get:Element(name = "link", required = false)
        @set:Element(name = "link", required = false)
        var link: String? = null

        @get:Attribute(name = "href", required = false)
        @set:Attribute(name = "href", required = false)
        var href: String? = null


    }

    class Category {
        @get:Attribute(name = "text", required = false)
        @set:Attribute(name = "text", required = false)
        var text: String? = null

        @get:Element(name = "category", required = false)
        @set:Element(name = "category", required = false)
        var category: Category? = null


    }

    class Episode : Serializable {
        @get:Element(name = "title", required = false)
        @set:Element(name = "title", required = false)
        var title: String? = null


        @get:Element(name = "pubDate", required = false)
        @set:Element(name = "pubDate", required = false)
        var pubDate: String? = null

        val publishedDate: String
            get() {
                val date = SimpleDateFormat("E, d MMM yyyy HH:mm:ss Z", Locale.US).parse(pubDate)
                return SimpleDateFormat("EEE, MMM dd, yyyy", Locale.US).format(date)
            }

        @get:Element(name = "link", required = false)
        @set:Element(name = "link", required = false)
        var link: String? = null

        @get:Element(name = "image", required = false)
        @set:Element(name = "image", required = false)
        var image: Image? = null

        @get:Element(name = "description", required = false)
        @set:Element(name = "description", required = false)
        var description: String? = null


        @get:Element(name = "enclosure", required = false)
        @set:Element(name = "enclosure", required = false)
        var enclosure: Enclosure? = null

        @get:Element(name = "duration", required = false)
        @set:Element(name = "duration", required = false)
        var duration: String? = null

        @get:Element(name = "keywords", required = false)
        @set:Element(name = "keywords", required = false)
        var keywords: String? = null


        @get:Element(name = "summary", required = false)
        @set:Element(name = "summary", required = false)
        var summary: String? = null


        @get:Element(name = "episode", required = false)
        @set:Element(name = "episode", required = false)
        var episode: String? = null


        @get:Element(name = "author", required = false)
        @set:Element(name = "author", required = false)
        var author: String? = null


    }


    class Enclosure {
        @get:Attribute(name = "length", required = false)
        @set:Attribute(name = "length", required = false)
        var length: String? = null

        @get:Attribute(name = "type", required = false)
        @set:Attribute(name = "type", required = false)
        var type: String? = null

        @get:Attribute(name = "url", required = false)
        @set:Attribute(name = "url", required = false)
        var url: String? = null


    }

}