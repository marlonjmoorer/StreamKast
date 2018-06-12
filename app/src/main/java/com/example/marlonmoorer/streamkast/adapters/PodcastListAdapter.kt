package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.databinding.ItemPodcastBinding
import com.example.marlonmoorer.streamkast.listeners.OnPodcastClickListener


class PodcastListAdapter(private val listener:OnPodcastClickListener?=null):DataBoundAdapter<ItemPodcastBinding>(){


    private var showList: List<MediaItem>?=null
    override fun getItemCount()= showList?.size?:0

    override fun onBindViewHolder(holder: DataViewHolder<ItemPodcastBinding>, position: Int) {
        holder.binding.apply {
            show=showList?.get(position)
            handler=listener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder<ItemPodcastBinding> {
        val viewDataBinding: ItemPodcastBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_podcast, parent, false)
        return DataViewHolder(viewDataBinding)
    }

    fun setPostcasts(podcasts:List<MediaItem>){
        this.showList=podcasts
        notifyDataSetChanged()
    }


}