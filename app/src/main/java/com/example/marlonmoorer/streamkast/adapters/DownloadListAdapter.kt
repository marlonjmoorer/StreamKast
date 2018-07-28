package com.example.marlonmoorer.streamkast.adapters

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
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.databinding.ItemDownloadBinding

import com.example.marlonmoorer.streamkast.databinding.ItemEditViewBinding
import com.example.marlonmoorer.streamkast.models.DownloadedEpisodeModel
import com.example.marlonmoorer.streamkast.viewModels.LibraryViewModel
import io.reactivex.subjects.PublishSubject

class DownloadListAdapter:EditableAdapter<DownloadListAdapter.ViewHolder>(){


    private var episodes:List<DownloadedEpisodeModel>? = null
    val clickEvent= PublishSubject.create<DownloadedEpisodeModel>()
    val deleteEvent=PublishSubject.create<DownloadedEpisodeModel>()

    private val deletedItems:MutableList<DownloadedEpisodeModel> = mutableListOf()


    override fun setEditeMode(canEdit:Boolean){
        super.setEditeMode(canEdit)
        deletedItems.clear()
    }

    fun commitDeletion(){
        deletedItems.forEach {
            deleteEvent.onNext(it)
        }
    }


    override fun onBindViewHolder(holder: DownloadListAdapter.ViewHolder, position: Int) {
        episodes?.get(position)?.let {holder.bind(it)}
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

    override fun getItemCount() =episodes?.size?:0

    fun setEpisodes(episodes: List<DownloadedEpisodeModel>){
        this.episodes=episodes
        notifyDataSetChanged()
    }

    fun update(info: LibraryViewModel.DownloadInfo) {
        episodes?.find{ it.downloadId==info.id }?.run {
            status=info.status
            progress=info.progress
            notifyChange()
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("status")
        fun setStatus(view: TextView, status:Int){
            view.text=when(status){
                DownloadManager.STATUS_PENDING->"Pending"
                DownloadManager.STATUS_RUNNING->"Downloading"
                DownloadManager.STATUS_SUCCESSFUL->"Ready"
                else->""
            }
        }
    }



    inner class ViewHolder(val binding: ViewDataBinding):RecyclerView.ViewHolder(binding.root){


        init {
            if(!editMode){
                binding.root.setOnClickListener {
                    episodes?.get(layoutPosition)?.let {clickEvent.onNext(it)}
                }
                binding.root.setOnLongClickListener {v->
                    openContextMenu(v)
                }
            }else{
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
        private fun openContextMenu(v:View):Boolean{
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

        fun bind(model: DownloadedEpisodeModel){
            if(binding is ItemDownloadBinding){
                binding.episode=model
            }
            if (binding is ItemEditViewBinding){
                binding.episode=model
            }
        }
    }

}