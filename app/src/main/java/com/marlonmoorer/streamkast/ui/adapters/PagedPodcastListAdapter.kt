package com.marlonmoorer.streamkast.ui.adapters

import android.arch.paging.PagedList
import android.arch.paging.PagedListAdapter
import android.databinding.DataBindingUtil
import android.databinding.OnRebindCallback
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marlonmoorer.streamkast.R
import com.marlonmoorer.streamkast.api.models.Podcast
import com.marlonmoorer.streamkast.databinding.ItemPodcastBinding
import io.reactivex.subjects.PublishSubject


class PagedPodcastListAdapter(var callback: PodcastListCallBack?=null):PagedListAdapter<Podcast,PagedPodcastListAdapter.ViewHolder>(diffCallback){


    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
       holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder{
        val binding: ItemPodcastBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_podcast, parent, false)
        return ViewHolder(binding)
    }



    interface PodcastListCallBack{
        fun onOpen(id:String)
    }


    inner class  ViewHolder(val binding: ItemPodcastBinding):RecyclerView.ViewHolder(binding.root){

        init {
            binding.setListener{v->
                callback?.onOpen(getItem(layoutPosition)!!.collectionId)
            }
            binding.subscribeButton.visibility= View.GONE
        }

        fun bind(model: Podcast?){
            binding.podcast=model
        }

    }

    companion object {
        var diffCallback=object:DiffUtil.ItemCallback<Podcast>(){
            override fun areItemsTheSame(oldItem: Podcast, newItem: Podcast): Boolean {
              return  oldItem.collectionId==newItem.collectionId
            }

            override fun areContentsTheSame(oldItem: Podcast?, newItem: Podcast?): Boolean {
                return  oldItem?.collectionId==newItem?.collectionId
            }
        }
    }

}