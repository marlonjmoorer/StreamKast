package com.example.marlonmoorer.streamkast.ui.adapters

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

class HistoryListAdapter(var callback:AdapterCallback?=null):EditableAdapter<HistoryListAdapter.ViewHolder>(){
    private var episodes:List<PlaybackHistory> = mutableListOf()
    private val seletedItems:MutableList<PlaybackHistory> = mutableListOf()

    override fun setEditeMode(canEdit: Boolean) {
        super.setEditeMode(canEdit)
        if(!canEdit)
            seletedItems.clear()
    }
    override fun getItemCount()=  episodes.size

    fun commitDeletion(){
        seletedItems.forEach {
            callback?.onDelete(it)
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(episodes[position])
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

    interface AdapterCallback{
        fun onOpen(episode: IEpisode)

        fun onDelete(episode: IEpisode)

        fun onLongClick(episode: IEpisode)
    }

    inner class ViewHolder(val binding: ViewDataBinding):RecyclerView.ViewHolder(binding.root){

        val currentEpisode
            get() = episodes[layoutPosition]

        init{
            if(!editMode){
                binding.root.setOnClickListener {
                       callback?.onOpen(currentEpisode)

                }
                binding.root.setOnLongClickListener {
                    seletedItems.add(currentEpisode)
                    callback?.onLongClick(currentEpisode)
                    return@setOnLongClickListener true
                }
            }
            else{

                (binding as ItemEditViewBinding).run{
                    root.setOnClickListener {
                        checkBox.toggle()
                    }
                    checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                        if(isChecked){
                            //if(!seletedItems.contains(currentEpisode))
                            seletedItems.add(currentEpisode)

                        }else{
                            seletedItems.remove(currentEpisode)
                        }
                    }
                }
            }

        }


        fun bind(model:PlaybackHistory){
            if (binding is ItemEditViewBinding){
                binding.episode=model
                if(seletedItems.contains(model)){
                    binding.checkBox.isChecked=true
                }

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
                            callback?.onDelete(currentEpisode)
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