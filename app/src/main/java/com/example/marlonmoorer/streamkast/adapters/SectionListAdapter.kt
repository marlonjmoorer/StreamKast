package com.example.marlonmoorer.streamkast.adapters

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.marlonmoorer.streamkast.R
import kotlinx.android.synthetic.main.item_section.view.*
import com.example.marlonmoorer.streamkast.viewModels.FeatureViewModel


/**
 * Created by marlonmoorer on 3/21/18.
 */
class SectionListAdapter(private var collections:MutableList<SectionModel>,val viewModel: FeatureViewModel): RecyclerView.Adapter<SectionListAdapter.ViewHolder>() {


    private var layoutManager: RecyclerView.LayoutManager? = null



    override fun getItemCount()= collections.size


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        var collection=  collections[position]
        holder?.view?.apply {
            if(section.layoutManager==null)section?.layoutManager=layoutManager
            section?.adapter=SectionAdapter(collection.items)
            section_header.text= collection.genre?.displayname()?:collection.title

            more_link.setOnClickListener{
               // viewModel

                viewModel.loadMore(collection.genre)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        //layoutManager=LinearLayoutManager(parent!!.context,LinearLayoutManager.HORIZONTAL,false)
        layoutManager= GridLayoutManager(parent!!.context,3)
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