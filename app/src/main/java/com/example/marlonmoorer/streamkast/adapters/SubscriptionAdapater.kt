package com.example.marlonmoorer.streamkast.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.data.Subscription
import com.example.marlonmoorer.streamkast.databinding.ItemFeaturedTileBinding
import com.example.marlonmoorer.streamkast.databinding.ItemSubTileBinding

class SubscriptionAdapater:DataBoundAdapter<ItemSubTileBinding>() {

    private var subs:List<Subscription>?=null
    fun setSubList(subs:List<Subscription>){
        this.subs=subs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataViewHolder<ItemSubTileBinding> {
        val binding=   ItemSubTileBinding.inflate(LayoutInflater.from(parent?.context))
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder<ItemSubTileBinding>?, position: Int) {
        holder?.binding.apply {
           this?.subscription =subs?.get(position)
        }
    }

    override fun getItemCount()=subs?.size?:0
}