package com.example.marlonmoorer.streamkast.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.models.MediaItem

import android.widget.LinearLayout
import com.example.marlonmoorer.streamkast.load
import kotlinx.android.synthetic.main.item_podcast_tile2.view.*
import org.jetbrains.anko.dip


/**
 * Created by marlonmoorer on 3/21/18.
 */
open class SectionAdapter(private val data: List<MediaItem>) :
        RecyclerView.Adapter<SectionAdapter.ViewHolder>() {



    class ViewHolder(val view:View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_podcast_tile2, parent, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var show=   data[position]
        holder.view?.apply {
            //show_title.text= show.collectionName
            show.artworkUrl100?.let {
                show_image.load(it)
            }
           // show_author.text=show.artistName
        }



    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = data.size
}


