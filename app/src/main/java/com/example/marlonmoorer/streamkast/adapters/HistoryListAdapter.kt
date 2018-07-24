package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.data.PlaybackHistory
import com.example.marlonmoorer.streamkast.databinding.ItemHistoryBinding
import com.example.marlonmoorer.streamkast.models.IEpisode
import io.reactivex.subjects.PublishSubject

class HistoryListAdapter():RecyclerView.Adapter<HistoryListAdapter.ViewHolder> (){
    private var episodes:List<PlaybackHistory>? = null
    override fun getItemCount()=  episodes?.size?:0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            episodes?.get(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding: ItemHistoryBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_history, parent, false)
        return ViewHolder(viewDataBinding)
    }

    fun setEpisodes(episodes: List<PlaybackHistory>){
        this.episodes=episodes
        notifyDataSetChanged()
    }

    val clickEvent= PublishSubject.create<IEpisode>()

    inner class ViewHolder(val binding: ItemHistoryBinding):RecyclerView.ViewHolder(binding.root){
        init{
            binding.root.setOnClickListener {
                episodes?.get(layoutPosition)?.let {clickEvent.onNext(it) }
            }
        }

        fun bind(model:PlaybackHistory){
            binding.episode=model
        }
    }

}