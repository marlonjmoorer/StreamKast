package com.marlonmoorer.streamkast.ui.adapters

import android.app.DownloadManager
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.marlonmoorer.streamkast.R
import com.marlonmoorer.streamkast.databinding.ItemDownloadBinding

import com.marlonmoorer.streamkast.databinding.ItemEditViewBinding
import com.marlonmoorer.streamkast.models.DownloadedEpisodeModel
import com.marlonmoorer.streamkast.ui.viewModels.LibraryViewModel
import kotlinx.android.synthetic.main.fragment_subscription.view.*

class DownloadListAdapter(val callback:EditAdapterCallback?=null):EditableAdapter<DownloadListAdapter.ViewHolder>(){


    private var episodes:List<DownloadedEpisodeModel> = mutableListOf()


    private val selectedItems:MutableList<DownloadedEpisodeModel> = mutableListOf()


    override fun setEditeMode(canEdit:Boolean){
        super.setEditeMode(canEdit)
        if(!canEdit)
            selectedItems.clear()
    }

    fun commitDeletion(){
        selectedItems.forEach {
          callback?.onDelete(it)
        }
    }


    override fun onBindViewHolder(holder: DownloadListAdapter.ViewHolder, position: Int) {
       holder.bind(episodes[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding:ViewDataBinding
        val id:Int
        if(viewType==EDITMODE)
          id=R.layout.item_edit_view
        else
            id=R.layout.item_download
        viewDataBinding=DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), id, parent, false)
        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount() =episodes.size

    fun setEpisodes(episodes: List<DownloadedEpisodeModel>){
        this.episodes=episodes
        notifyDataSetChanged()
    }

    fun update(info: LibraryViewModel.DownloadInfo) {
        episodes.find{ it.downloadId==info.id }?.run {
            status=info.status
            progress=info.progress
            notifyChange()
        }
    }




    inner class ViewHolder(val binding: ViewDataBinding):RecyclerView.ViewHolder(binding.root){

        val currentEpisode
            get() = episodes[layoutPosition]

        init {
            if(!editMode){
                binding.root.setOnClickListener {
                   callback?.onOpen(currentEpisode)
                }
                binding.root.setOnLongClickListener {v->
                    selectedItems.add(currentEpisode)
                    callback?.onLongClick(currentEpisode)
                    return@setOnLongClickListener true
                }
            }else{
                (binding as ItemEditViewBinding).run{
                    root.setOnClickListener {
                        checkBox.toggle()
                    }
                    checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                        if(isChecked){
                            selectedItems.add(currentEpisode)
                        }else{
                            selectedItems.remove(currentEpisode)
                        }
                    }
                }
            }
        }


        fun bind(model: DownloadedEpisodeModel){
            if(binding is ItemDownloadBinding){
                binding.episode=model

            }
            if (binding is ItemEditViewBinding){
                binding.episode=model
                if(selectedItems.contains(model)){
                    binding.checkBox.isChecked=true
                }
            }
        }
    }

}