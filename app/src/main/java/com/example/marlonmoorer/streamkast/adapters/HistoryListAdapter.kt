package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.data.PlaybackHistory
import com.example.marlonmoorer.streamkast.databinding.ItemEpisodeBinding
import com.example.marlonmoorer.streamkast.databinding.ItemHistoryBinding
import com.example.marlonmoorer.streamkast.listeners.IEpisodeListener
import com.example.marlonmoorer.streamkast.models.IEpisode

class HistoryListAdapter(val listener:IEpisodeListener):DataBoundAdapter<ItemHistoryBinding> (){
    private var episodes:List<PlaybackHistory>? = null
    override fun getItemCount()=  episodes?.size?:0

    override fun onBindViewHolder(holder: DataViewHolder<ItemHistoryBinding>, position: Int) {
        holder.binding.apply {
            episode= episodes?.get(position)
            handler=listener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder<ItemHistoryBinding> {
        val viewDataBinding: ItemHistoryBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_history, parent, false)
        return DataViewHolder(viewDataBinding)
    }

    fun setEpisodes(episodes: List<PlaybackHistory>){
        this.episodes=episodes
        notifyDataSetChanged()
    }

}