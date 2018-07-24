package com.example.marlonmoorer.streamkast.adapters

import android.arch.paging.PagedListAdapter
import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.models.Podcast
import com.example.marlonmoorer.streamkast.databinding.ItemPodcastBinding
import io.reactivex.subjects.PublishSubject


class PagedPodcastListAdapter:PagedListAdapter<Podcast,PagedPodcastListAdapter.ViewHolder>(diffCallback){


    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
       holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder{
        val binding: ItemPodcastBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_podcast, parent, false)
        return ViewHolder(binding)
    }

    val openEvent=PublishSubject.create<Podcast>()
    val toggleSubEvent=PublishSubject.create<Podcast>()

    inner class  ViewHolder(val binding: ItemPodcastBinding):RecyclerView.ViewHolder(binding.root){

        init {
            binding.setListener{v->
                val p = getItem(layoutPosition)!!
                when(v.id){
                    R.id.subscribe_button->toggleSubEvent.onNext(p)
                    else->openEvent.onNext(p)
                }
            }
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