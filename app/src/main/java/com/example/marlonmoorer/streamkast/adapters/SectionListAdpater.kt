package com.example.marlonmoorer.streamkast.adapters

import android.content.Context
import android.media.browse.MediaBrowser
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.load
import kotlinx.android.synthetic.main.item_podcast.view.*
import kotlinx.android.synthetic.main.item_section.view.*

/**
 * Created by marlonmoorer on 3/21/18.
 */
class SectionListAdpater(private var collections:MutableList<SectionModel>, val context: Context): RecyclerView.Adapter<SectionListAdpater.ViewHolder>() {


    override fun getItemCount()= collections.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        var collection=  collections[position]
        holder?.view?.apply {
            section?.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            section?.adapter=SectionAdapter(collection.items)
            section_header.text=collection.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_section, parent, false)

        return ViewHolder(view)
    }

    fun addSection(section: SectionModel){
        collections.add(section)
        notifyDataSetChanged()
    }
    fun prependSection(section: SectionModel){
        collections= (listOf(section)+collections).toMutableList()
        notifyDataSetChanged()
    }


}