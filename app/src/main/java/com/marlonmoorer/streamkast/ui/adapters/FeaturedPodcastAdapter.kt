package com.marlonmoorer.streamkast.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.marlonmoorer.streamkast.data.Featured
import com.marlonmoorer.streamkast.databinding.ItemFeaturedTileBinding
import io.reactivex.subjects.PublishSubject


/**
 * Created by marlonmoorer on 3/21/18.
 */
class FeaturedPodcastAdapter :RecyclerView.Adapter<FeaturedPodcastAdapter.ViewHolder>(){
    private var podcasts:List<Featured>?=null
    fun setPodcasts(podcasts:List<Featured>){
        this.podcasts=podcasts
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val viewBinding=ItemFeaturedTileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        podcasts?.get(position)?.let {holder.bind(it)}

    }


    override fun getItemCount() = podcasts?.size?:0

    val clickEvent= PublishSubject.create<Featured>()

    inner class ViewHolder(val binding: ItemFeaturedTileBinding):RecyclerView.ViewHolder(binding.root) {

        init {
            binding.podcastImage.setOnClickListener {
                podcasts?.get(layoutPosition)?.let {
                    clickEvent.onNext(it)
                }
            }
        }

        fun bind(model: Featured) {
            binding.featured = model
        }
    }
}


