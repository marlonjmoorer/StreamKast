package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.models.*

import com.example.marlonmoorer.streamkast.databinding.ItemEpisodeBinding
import com.example.marlonmoorer.streamkast.listeners.IEpisodeListener


/**
 * Created by marlonmoorer on 3/29/18.
 */
class EpisodeListAdapter(val listener:IEpisodeListener):DataBoundAdapter<ItemEpisodeBinding> (){
    private var episodes:List<Episode>? = null
    override fun getItemCount()=  episodes?.size?:0

    override fun onBindViewHolder(holder: DataViewHolder<ItemEpisodeBinding>, position: Int) {
        holder.binding.apply {
            episode=episodes?.get(position)
            handler=listener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder<ItemEpisodeBinding> {
        val viewDataBinding:ItemEpisodeBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_episode, parent, false)

        return DataViewHolder(viewDataBinding)
    }

    fun setEpisodes(episodes: List<Episode>){
        this.episodes=episodes
        notifyDataSetChanged()
    }

}



