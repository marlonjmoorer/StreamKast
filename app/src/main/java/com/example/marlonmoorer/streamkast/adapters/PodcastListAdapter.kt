package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.BR
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.models.Podcast

import com.example.marlonmoorer.streamkast.databinding.ItemPodcastBinding
import com.example.marlonmoorer.streamkast.listeners.IPodcastListener



class PodcastListAdapter(private val listener:IPodcastListener?=null):DataBoundAdapter<ItemPodcastBinding>(){


    private var podcastList: List<Podcast>?=null
    override fun getItemCount()= podcastList?.size?:0
    override fun onBindViewHolder(holder: DataViewHolder<ItemPodcastBinding>, position: Int) {
        val podcast= podcastList?.get(position)
        holder.binding.run{
            show=podcast
            handler=listener
        }
        podcast?.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(observable: Observable, id: Int) {
                if(id== BR.subscribed){
                    holder.itemView?.let{
                        val messageId=if(podcast.subscribed) R.string.message_subscribe else R.string.message_unsubscribe
                        val message=it.resources.getString(messageId)
                        Snackbar.make(it,message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        })

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder<ItemPodcastBinding> {
        val viewDataBinding: ItemPodcastBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_podcast, parent, false)
        return DataViewHolder(viewDataBinding)
    }

    fun setPostcasts(podcasts:List<Podcast>){
        this.podcastList=podcasts
        notifyDataSetChanged()
    }


}