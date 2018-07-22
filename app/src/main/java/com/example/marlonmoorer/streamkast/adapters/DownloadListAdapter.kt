package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.databinding.ItemDownloadBinding
import com.example.marlonmoorer.streamkast.listeners.IEpisodeListener
import com.example.marlonmoorer.streamkast.viewModels.LibraryViewModel

class DownloadListAdapter(private var listener: IEpisodeListener) :DataBoundAdapter<ItemDownloadBinding>(){

    private var episodes:List<LibraryViewModel.DownloadedEpisodeModel>? = null
    override fun onBindViewHolder(holder: DataViewHolder<ItemDownloadBinding>, position: Int) {
        holder.binding.apply {
            episode= episodes!![position]
            handler=listener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder<ItemDownloadBinding> {
        val viewDataBinding:ItemDownloadBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_download, parent, false)
        return DataViewHolder(viewDataBinding)
    }

    override fun getItemCount() =episodes?.size?:0

    fun setEpisodes(episodes: List<LibraryViewModel.DownloadedEpisodeModel>){
        this.episodes=episodes
        notifyDataSetChanged()
    }

}