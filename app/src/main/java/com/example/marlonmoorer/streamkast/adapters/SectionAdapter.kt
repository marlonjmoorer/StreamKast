package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.databinding.ItemPodcastTileBinding

import com.example.marlonmoorer.streamkast.load
import kotlinx.android.synthetic.main.item_podcast_tile.view.*


/**
 * Created by marlonmoorer on 3/21/18.
 */
open class SectionAdapter(private val model:SectionModel) :
        RecyclerView.Adapter<DataViewHolder<ItemPodcastTileBinding>>() {





    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DataViewHolder<ItemPodcastTileBinding> {
        val viewBinding:ItemPodcastTileBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.context)
                ,R.layout.item_podcast_tile, parent, false)

        return DataViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: DataViewHolder<ItemPodcastTileBinding>, position: Int) {

        var show=  model.items[position]
        holder.binding?.apply {
            section=model
            media=show
            show.artworkUrl100?.let {
               showImage.load(it)
            }

        }

    }


    override fun getItemCount() = model.items.size
}


