package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.databinding.ItemPodcastBinding
import com.example.marlonmoorer.streamkast.load
import kotlinx.android.synthetic.main.item_podcast.view.*



class PodcastListAdapter(private val data: List<MediaItem>):RecyclerView.Adapter<DataViewHolder<ItemPodcastBinding>>(){


    override fun getItemCount()=data.size

    override fun onBindViewHolder(holder: DataViewHolder<ItemPodcastBinding>, position: Int) {

        var show=data[position]
        holder?.binding.show=show

//       holder.view.apply {
//           item_title.text=show.collectionName
//           show.artworkUrl100?.let {
//               if (item_image.drawable==null){
//                   item_image.load(it)
//               }
//
//           }
//       }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder<ItemPodcastBinding> {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_podcast, parent, false)
            val viewDataBinding: ItemPodcastBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_podcast, parent, false)
            return DataViewHolder(viewDataBinding)
    }



}