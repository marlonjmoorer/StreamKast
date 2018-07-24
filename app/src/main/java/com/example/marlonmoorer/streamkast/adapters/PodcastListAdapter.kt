package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.models.Podcast

import com.example.marlonmoorer.streamkast.databinding.ItemPodcastBinding
import io.reactivex.subjects.PublishSubject


class PodcastListAdapter:RecyclerView.Adapter<PodcastListAdapter.ViewHolder>(){


    private var podcastList: List<Podcast>?=null

    override fun getItemCount()= podcastList?.size?:0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(podcastList?.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val viewDataBinding: ItemPodcastBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_podcast, parent, false)
        return ViewHolder(viewDataBinding)
    }

    fun setPostcasts(podcasts:List<Podcast>){
        this.podcastList=podcasts
        notifyDataSetChanged()
    }

    val openEvent= PublishSubject.create<Podcast>()
    val toggleSubEvent= PublishSubject.create<Podcast>()
    inner class  ViewHolder(val binding: ItemPodcastBinding):RecyclerView.ViewHolder(binding.root){

        init {
            binding.setListener {v->
                podcastList?.get(layoutPosition)?.let {p->
                    when(v.id){
                        R.id.subscribe_button->toggleSubEvent.onNext(p)
                        else->openEvent.onNext(p)
                    }
                }
            }
        }

        fun bind(model: Podcast?){
            binding.podcast=model
        }

    }

}