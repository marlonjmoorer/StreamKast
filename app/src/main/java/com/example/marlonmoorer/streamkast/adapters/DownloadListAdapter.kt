package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.databinding.ItemDownloadBinding
import com.example.marlonmoorer.streamkast.models.DownloadedEpisodeModel
import io.reactivex.subjects.PublishSubject

class DownloadListAdapter:RecyclerView.Adapter<DownloadListAdapter.ViewHolder>(){

    private var episodes:List<DownloadedEpisodeModel>? = null
    val clickEvent= PublishSubject.create<DownloadedEpisodeModel>()
    val deleteEvent=PublishSubject.create<DownloadedEpisodeModel>()

    override fun onBindViewHolder(holder: DownloadListAdapter.ViewHolder, position: Int) {
        episodes?.get(position)?.let {holder.bind(it)}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding:ItemDownloadBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_download, parent, false)
        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount() =episodes?.size?:0

    fun setEpisodes(episodes: List<DownloadedEpisodeModel>){
        this.episodes=episodes
        notifyDataSetChanged()
    }



    inner class ViewHolder(val binding: ItemDownloadBinding):RecyclerView.ViewHolder(binding.root){


        init {
            binding.root.setOnClickListener {
                episodes?.get(layoutPosition)?.let {clickEvent.onNext(it)}
            }
            binding.root.setOnLongClickListener {v->
                openContextMenu(v)
            }
        }
        fun openContextMenu(v:View):Boolean{
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
             binding.episode=model
        }
    }

}