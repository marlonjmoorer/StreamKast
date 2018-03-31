package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.models.*
import com.example.marlonmoorer.streamkast.databinding.ItemEpisodeBinding

/**
 * Created by marlonmoorer on 3/29/18.
 */
class EpisodeListAdapter(val episodes:List<Episode>):RecyclerView.Adapter<DataViewHolder<ItemEpisodeBinding>> (){

    override fun getItemCount()=  episodes.size

    override fun onBindViewHolder(holder: DataViewHolder<ItemEpisodeBinding>, position: Int) {

        var data= episodes[position]
        holder?.binding.apply {
            episode=data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder<ItemEpisodeBinding> {

        val viewDataBinding:ItemEpisodeBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_episode, parent, false)
        return DataViewHolder(viewDataBinding)
    }

}



