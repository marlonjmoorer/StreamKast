package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.models.*
import com.example.marlonmoorer.streamkast.data.Featured

import com.example.marlonmoorer.streamkast.databinding.ItemEpisodeBinding

import com.example.marlonmoorer.streamkast.models.IEpisode
import io.reactivex.subjects.PublishSubject


/**
 * Created by marlonmoorer on 3/29/18.
 */
class EpisodeListAdapter:RecyclerView.Adapter<EpisodeListAdapter.ViewHolder> (){
    private var episodes:List<IEpisode>? = null
    override fun getItemCount()=  episodes?.size?:0

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        holder.bind(episodes?.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding:ItemEpisodeBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_episode, parent, false)
        return ViewHolder(viewDataBinding)
    }

    fun setEpisodes(episodes: List<IEpisode>){
        this.episodes=episodes
        notifyDataSetChanged()
    }

    val openEvent= PublishSubject.create<IEpisode>()
    val playEvent = PublishSubject.create<IEpisode>()
    inner class ViewHolder(val binding: ItemEpisodeBinding):RecyclerView.ViewHolder(binding.root){
        init {
            binding.setListener {v->
                episodes?.get(layoutPosition)?.let {p->
                    when(v.id){
                        R.id.play_button->playEvent.onNext(p)
                        else->openEvent.onNext(p)
                    }
                }
            }
        }

        fun bind(episode: IEpisode?){
            binding.episode=episode
        }
    }

}



