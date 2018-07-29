package com.example.marlonmoorer.streamkast.ui.adapters

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.*
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.data.Subscription
import com.example.marlonmoorer.streamkast.databinding.ItemEditViewBinding
import com.example.marlonmoorer.streamkast.databinding.ItemHistoryBinding
import com.example.marlonmoorer.streamkast.databinding.ItemSelectableSubBinding
import com.example.marlonmoorer.streamkast.databinding.ItemSubTileBinding

class SubscriptionAdapater(var callback:SubscriptionAdapterCallback?=null):EditableAdapter<SubscriptionAdapater.ViewHolder>() {

    private var subs:List<Subscription> = mutableListOf()
    private val selectedItems:MutableList<Subscription> = mutableListOf()
    fun setSubList(subs:List<Subscription>){
        this.subs=subs
        notifyDataSetChanged()
    }

    override fun setEditeMode(canEdit:Boolean){
        super.setEditeMode(canEdit)
        if(!canEdit)
            selectedItems.clear()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= when(viewType){
            EDITMODE->    ItemSelectableSubBinding.inflate(LayoutInflater.from(parent.context))
            else->ItemSubTileBinding.inflate(LayoutInflater.from(parent.context))
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(subs[position])
    }

    override fun getItemCount()=subs.size

    private fun handleContextMenu(context: Context,view:View,subscription: Subscription)= with(PopupMenu(context,view)){
        menuInflater.inflate(R.menu.menu_subscription,menu)
        setOnMenuItemClickListener { item->
            when(item.itemId){
                R.id.action_unsubscribe->{
                    callback?.onUnsubscribe(subscription)
                }
            }
            return@setOnMenuItemClickListener true
        }
        show()
    }

    fun commitUnsinscribe() {
        selectedItems.forEach {
            callback?.onUnsubscribe(it)
        }
    }


    inner class  ViewHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root){

        val currentSubscription
            get()=subs[layoutPosition]
        init {
            when(binding){
                is ItemSubTileBinding->{
                    binding.setListener {v->
                        when(v.id){
                            R.id.menu->handleContextMenu(itemView.context,v,currentSubscription)
                            else->callback?.onOpen("${currentSubscription.podcastId}")
                        }
                    }
                    binding.root.setOnLongClickListener {v->
                        handleContextMenu(itemView.context,v,currentSubscription)
                        return@setOnLongClickListener true
                    }
                }
                is ItemSelectableSubBinding->{
                    binding.overlay.setOnClickListener {
                        binding.checkBox.toggle()
                    }
                    binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked){
                            selectedItems.add(currentSubscription)
                        }else{
                            selectedItems.remove(currentSubscription)
                        }
                    }
                }
            }

        }

        fun bind(model: Subscription?){
            when(binding){
                is ItemSubTileBinding->binding.subscription=model
                is ItemSelectableSubBinding->{
                    binding.subscription=model
                    binding.checkBox.isChecked=selectedItems.contains(currentSubscription)
                }
            }
        }
    }


    interface SubscriptionAdapterCallback{
        fun onOpen(id:String)
        fun onUnsubscribe(subscription: Subscription)
    }



}