package com.example.marlonmoorer.streamkast.ui.adapters

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.*
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.data.Subscription
import com.example.marlonmoorer.streamkast.databinding.ItemSubTileBinding
import io.reactivex.subjects.PublishSubject

class SubscriptionAdapater():RecyclerView.Adapter<SubscriptionAdapater.ViewHolder>() {

    private var subs:List<Subscription>?=null
    fun setSubList(subs:List<Subscription>){
        this.subs=subs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= ItemSubTileBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(subs?.get(position))

    }

    override fun getItemCount()=subs?.size?:0

    fun handleContextMenu(context: Context,view:View,subscription: Subscription)= with(PopupMenu(context,view)){
        menuInflater.inflate(R.menu.menu_subscription,menu)
        setOnMenuItemClickListener { item->
            when(item.itemId){
                R.id.action_unsubscribe->{
                    toggleSubEvent.onNext(subscription)
                    view.let {
                        Snackbar.make(it, "Unsubscribed", Snackbar.LENGTH_LONG)
                        .setAction("Undo",{
                            toggleSubEvent.onNext(subscription)
                        }).show()
                    }
                }
                else->{}
            }
            return@setOnMenuItemClickListener true
        }
        show()
    }

    val openEvent= PublishSubject.create<Subscription>()
    val toggleSubEvent= PublishSubject.create<Subscription>()
    inner class  ViewHolder(val binding: ItemSubTileBinding): RecyclerView.ViewHolder(binding.root){

        init {
            binding.setListener {v->
                subs?.get(layoutPosition)?.let {p->
                    when(v.id){
                        R.id.menu->handleContextMenu(itemView.context,v,p)
                        else->openEvent.onNext(p)
                    }
                }
            }
        }



        fun bind(model: Subscription?){
            binding.subscription=model
        }

    }


}