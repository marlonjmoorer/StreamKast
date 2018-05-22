package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.api.models.chart.PodcastEntry
import com.example.marlonmoorer.streamkast.databinding.ItemPodcastTileBinding
import com.example.marlonmoorer.streamkast.load

import com.example.marlonmoorer.streamkast.viewModels.BrowseViewModel


/**
 * Created by marlonmoorer on 3/21/18.
 */
open class SectionAdapter(private val shows:List<PodcastEntry>) :
        DataBoundAdapter<ItemPodcastTileBinding>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DataViewHolder<ItemPodcastTileBinding> {

        val viewBinding:ItemPodcastTileBinding= ItemPodcastTileBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return DataViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: DataViewHolder<ItemPodcastTileBinding>, position: Int) {

        val show=  shows[position]!!
        holder.binding?.apply{
            media=show
        }
    }


    override fun getItemCount() = shows.size
}


