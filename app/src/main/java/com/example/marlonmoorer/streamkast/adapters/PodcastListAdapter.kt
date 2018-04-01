package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.databinding.ItemPodcastBinding
import com.example.marlonmoorer.streamkast.load
import com.example.marlonmoorer.streamkast.viewModels.SectionViewModel


class PodcastListAdapter(private val showList: List<MediaItem>,val model: SectionViewModel):DataBoundAdapter<ItemPodcastBinding>(){



    override fun getItemCount()= showList.size

    override fun onBindViewHolder(holder: DataViewHolder<ItemPodcastBinding>, position: Int) {

        var data= showList[position]
        holder?.binding.apply {
            show=data
            itemImage.load(data.artworkUrl100!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder<ItemPodcastBinding> {
        val viewDataBinding: ItemPodcastBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_podcast, parent, false)
        viewDataBinding.viewModel= model
        return DataViewHolder(viewDataBinding)
    }



}