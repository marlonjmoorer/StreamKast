package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.databinding.ItemPodcastTileBinding
import com.example.marlonmoorer.streamkast.load

import com.example.marlonmoorer.streamkast.viewModels.SectionViewModel


/**
 * Created by marlonmoorer on 3/21/18.
 */
open class SectionAdapter(private val shows:List<MediaItem>, private val model: SectionViewModel) :
        DataBoundAdapter<ItemPodcastTileBinding>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DataViewHolder<ItemPodcastTileBinding> {
        val viewBinding:ItemPodcastTileBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.context)
                ,R.layout.item_podcast_tile, parent, false)
        viewBinding.viewModel=model
        return DataViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: DataViewHolder<ItemPodcastTileBinding>, position: Int) {

        val show=  shows[position]!!
        holder.binding?.apply{
            media=show
            show.artworkUrl100?.let {
                showImage.load(it)
            }
        }


    }


    override fun getItemCount() = shows.size
}


