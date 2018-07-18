package com.example.marlonmoorer.streamkast.api.models

import com.example.marlonmoorer.streamkast.models.IPodcast


class Channel:IPodcast {
    override var id=""
    override var title=""
    override var link=""
    override var description=""
    override var author=""
    override var thumbnail=""

    var categories:MutableList<String> = mutableListOf()
    var episodes:MutableList<Episode> = mutableListOf()
    val count
        get() = episodes.size.toString()

}


