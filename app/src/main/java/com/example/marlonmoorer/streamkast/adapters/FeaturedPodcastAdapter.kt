package com.example.marlonmoorer.streamkast.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.api.models.chart.PodcastEntry
import com.example.marlonmoorer.streamkast.databinding.ItemFeaturedTileBinding
import com.example.marlonmoorer.streamkast.listeners.OnPodcastClickListener


/**
 * Created by marlonmoorer on 3/21/18.
 */
open class FeaturedPodcastAdapter(val listener:OnPodcastClickListener?=null) :
        DataBoundAdapter<ItemFeaturedTileBinding>() {
    private var shows:List<PodcastEntry>?=null
    fun setPodCasts(podcasts:List<PodcastEntry>){
        this.shows=podcasts
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder<ItemFeaturedTileBinding> {
        val viewBinding=ItemFeaturedTileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: DataViewHolder<ItemFeaturedTileBinding>, position: Int) {
        holder.binding.apply{
            media=shows?.get(position)
            handler = listener
        }
    }


    override fun getItemCount() = shows?.size?:0
}


