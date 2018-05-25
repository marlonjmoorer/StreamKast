package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.models.chart.PodcastEntry
import com.example.marlonmoorer.streamkast.databinding.ItemSectionBinding


/**
 * Created by marlonmoorer on 3/21/18.
 */
class SectionListAdapter(private val shows:List<PodcastEntry>): DataBoundAdapter<ItemSectionBinding>() {

    init {


    }
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adpaters= mutableMapOf<String,FeaturedPodcastAdapter>()
    override fun getItemCount()= shows.size

    override fun onBindViewHolder(holder: DataViewHolder<ItemSectionBinding>, position: Int) {

//        var collection=  shows[position]
//        holder?.binding?.apply {
//            section.adapter=adpaters[collection.key]
//            sectionModel=collection
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataViewHolder<ItemSectionBinding> {
        layoutManager= GridLayoutManager(parent!!.context,3)
        val viewBinding:ItemSectionBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_section,parent,false)
        viewBinding.section.layoutManager=layoutManager
        //viewBinding.browseViewModel=model
        return DataViewHolder(viewBinding)
    }

    fun addSection(section: SectionModel){
        //collections.add(section)
       // adpaters.set(section.key, FeaturedPodcastAdapter(section.items,model))
        notifyDataSetChanged()
    }
    fun prependSection(section: SectionModel){
        //collections= (listOf(section)+collections).toMutableList()
       // adpaters.set(section.key,FeaturedPodcastAdapter(section.items,model))
        notifyDataSetChanged()
    }


}