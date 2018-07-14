package com.example.marlonmoorer.streamkast.adapters

import android.arch.paging.PagedListAdapter
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.support.design.widget.Snackbar
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.BR
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.models.Podcast
import com.example.marlonmoorer.streamkast.databinding.ItemPodcastBinding
import com.example.marlonmoorer.streamkast.listeners.IPodcastListener




class PagedPodcastListAdapter(private val listener: IPodcastListener?=null):PagedListAdapter<Podcast,DataBoundAdapter.DataViewHolder<ItemPodcastBinding>>(diffCallback){


    override fun onBindViewHolder(holder: DataBoundAdapter.DataViewHolder<ItemPodcastBinding>, position: Int) {
        val podcast= getItem(position)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundAdapter.DataViewHolder<ItemPodcastBinding> {
        val viewDataBinding: ItemPodcastBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_podcast, parent, false)
        return DataBoundAdapter.DataViewHolder(viewDataBinding)
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