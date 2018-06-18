package com.example.marlonmoorer.streamkast.adapters

import android.content.Context
import android.support.v7.widget.PopupMenu
import android.view.*
import android.widget.AdapterView
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.data.Subscription
import com.example.marlonmoorer.streamkast.databinding.ItemFeaturedTileBinding
import com.example.marlonmoorer.streamkast.databinding.ItemSubTileBinding
import com.example.marlonmoorer.streamkast.listeners.OnSubscriptionMenuListener
import kotlinx.android.synthetic.main.item_sub_tile.view.*

class SubscriptionAdapater(private val listener:OnSubscriptionMenuListener?=null):DataBoundAdapter<ItemSubTileBinding>() {

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
        holder?.itemView?.menu?.setOnClickListener { view->
            handleContextMenu(holder.itemView.context,view,subs!!.get(position))
        }
    }

    override fun getItemCount()=subs?.size?:0

    fun handleContextMenu(context: Context,view:View,subscription: Subscription)= with(PopupMenu(context,view)){
        menuInflater.inflate(R.menu.menu_subscription,menu)
        setOnMenuItemClickListener { item->
            when(item.itemId){
                R.id.action_unsubscribe->listener?.unSubscribe(subscription)
                else->{}
            }
            return@setOnMenuItemClickListener true
        }
        show()
    }


}