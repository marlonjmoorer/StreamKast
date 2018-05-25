package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.ISelectHandler
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.api.models.chart.PodcastEntry
import com.example.marlonmoorer.streamkast.databinding.ItemFeaturedTileBinding



/**
 * Created by marlonmoorer on 3/21/18.
 */
open class FeaturedPodcastAdapter(private val shows:List<PodcastEntry>) :
        DataBoundAdapter<ItemFeaturedTileBinding>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DataViewHolder<ItemFeaturedTileBinding> {

        val viewBinding=ItemFeaturedTileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: DataViewHolder<ItemFeaturedTileBinding>, position: Int) {

        val show=  shows[position]!!
        holder.binding?.apply{
            media=show
            selector= handler
        }

    }


    override fun getItemCount() = shows.size
}


