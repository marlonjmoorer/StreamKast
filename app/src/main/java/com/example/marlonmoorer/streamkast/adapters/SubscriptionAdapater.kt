package com.example.marlonmoorer.streamkast.adapters

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v7.widget.PopupMenu
import android.view.*
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.data.Subscription
import com.example.marlonmoorer.streamkast.databinding.ItemSubTileBinding
import com.example.marlonmoorer.streamkast.listeners.ISubscriptionListener
import kotlinx.android.synthetic.main.item_sub_tile.view.*

class SubscriptionAdapater(private val listener:ISubscriptionListener?=null):DataBoundAdapter<ItemSubTileBinding>() {

    private var subs:List<Subscription>?=null
    fun setSubList(subs:List<Subscription>){
        this.subs=subs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataViewHolder<ItemSubTileBinding> {
        val binding= ItemSubTileBinding.inflate(LayoutInflater.from(parent?.context))
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder<ItemSubTileBinding>?, position: Int) {
        holder?.binding.apply {
           this?.subscription =subs?.get(position)
        }
        holder?.itemView?.setOnClickListener {
            listener?.viewPodcast(subs!!.get(position).podcastId.toString())
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
                R.id.action_unsubscribe->{
                    listener?.unsubscribe(subscription)
                    view.let {
                        Snackbar.make(it, "Unsubscribed", Snackbar.LENGTH_LONG)
                        .setAction("Undo",{
                            listener?.subscribe(subscription)
                        }).show()
                    }
                }
                else->{}
            }
            return@setOnMenuItemClickListener true
        }
        show()
    }


}