package com.example.marlonmoorer.streamkast.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.load
import kotlinx.android.synthetic.main.item_podcast.view.*


class PodcastListAdapter(private val data: List<MediaItem>):SectionAdapter(data) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var show=data[position]
       holder.view.apply {
           item_title.text=show.collectionName
           show.artworkUrl100?.let {
               if (item_image.drawable==null){
                   item_image.load(it)
               }

           }
       }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_podcast, parent, false)
            return ViewHolder(view)
    }

}