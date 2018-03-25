package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.databinding.ItemSectionBinding



/**
 * Created by marlonmoorer on 3/21/18.
 */
class SectionListAdapter(private var collections:MutableList<SectionModel> = mutableListOf<SectionModel>()): RecyclerView.Adapter<DataViewHolder<ItemSectionBinding>>() {


    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun getItemCount()= collections.size




    override fun onBindViewHolder(holder: DataViewHolder<ItemSectionBinding>, position: Int) {
        var collection=  collections[position]
        holder?.binding?.apply {
            if(section.layoutManager==null)section?.layoutManager=layoutManager
            section?.adapter=SectionAdapter(collection)
            setSection(collection)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataViewHolder<ItemSectionBinding> {
        layoutManager= GridLayoutManager(parent!!.context,3)
        val viewBinding:ItemSectionBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_section,parent,false)
        return DataViewHolder(viewBinding)
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