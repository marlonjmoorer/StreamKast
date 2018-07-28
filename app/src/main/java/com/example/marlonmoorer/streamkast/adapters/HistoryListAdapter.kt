package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.data.PlaybackHistory

import com.example.marlonmoorer.streamkast.databinding.ItemEditViewBinding
import com.example.marlonmoorer.streamkast.databinding.ItemHistoryBinding
import com.example.marlonmoorer.streamkast.models.IEpisode
import io.reactivex.subjects.PublishSubject

class HistoryListAdapter:EditableAdapter<HistoryListAdapter.ViewHolder>(){
    private var episodes:List<PlaybackHistory>? = null

    private val deletedItems:MutableList<PlaybackHistory> = mutableListOf()

    override fun setEditeMode(canEdit: Boolean) {
       super.setEditeMode(canEdit)
        deletedItems.clear()
    }

    override fun getItemCount()=  episodes?.size?:0

    fun commitDeletion(){
        deletedItems.forEach {
            deleteEvent.onNext(it)
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        episodes?.get(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding:ViewDataBinding
        val id:Int
        if(viewType==EDITMODE)
            id=R.layout.item_edit_view
        else
            id=R.layout.item_history
        viewDataBinding=DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), id, parent, false)
        return ViewHolder(viewDataBinding)
    }


    fun setEpisodes(episodes: List<PlaybackHistory>){
        this.episodes=episodes
        notifyDataSetChanged()
    }

    val clickEvent= PublishSubject.create<IEpisode>()
    val deleteEvent=PublishSubject.create<PlaybackHistory>()

    inner class ViewHolder(val binding: ViewDataBinding):RecyclerView.ViewHolder(binding.root){
        init{
            if(!editMode){
                binding.root.setOnClickListener {
                    episodes?.get(layoutPosition)?.let {clickEvent.onNext(it) }
                }
                binding.root.setOnLongClickListener {v->
                    openContextMenu(v)
                }
            }
            else{

                (binding as ItemEditViewBinding).run{
                    root.setOnClickListener {
                        checkBox.toggle()
                    }
                    checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                        if(isChecked){
                            deletedItems.add(episodes!![layoutPosition])
                        }else{
                            deletedItems.remove(episodes!![layoutPosition])
                        }
                    }
                }
            }

        }

        fun bind(model:PlaybackHistory){
            if (binding is ItemEditViewBinding){
                binding.episode=model
            }else if(binding is ItemHistoryBinding){
                binding.episode=model
            }

        }

        private fun openContextMenu(v: View):Boolean{
            PopupMenu(v.context,v).run {
                inflate(R.menu.download_menu)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.action_remove->{
                            episodes?.get(layoutPosition)?.let {
                                deleteEvent.onNext(it)
                            }
                            return@setOnMenuItemClickListener true
                        }
                    }
                    return@setOnMenuItemClickListener false
                }
                show()
            }
            return true
        }
    }

}