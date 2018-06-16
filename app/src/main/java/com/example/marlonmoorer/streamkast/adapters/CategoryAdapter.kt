package com.example.marlonmoorer.streamkast.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.databinding.ItemCategoryBinding
import com.example.marlonmoorer.streamkast.listeners.OnGenreClickListener

/**
 * Created by marlonmoorer on 4/2/18.
 */
class CategoryAdapter(val listener: OnGenreClickListener?=null):DataBoundAdapter<ItemCategoryBinding>() {

    var categories=MediaGenre.categories
    override fun onBindViewHolder(holder: DataViewHolder<ItemCategoryBinding>?, position: Int) {
        holder?.binding?.apply {
            genre=categories.get(position)
            handler=listener
        }
    }

    override fun getItemCount()= categories.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataViewHolder<ItemCategoryBinding> {
        val viewBinding = ItemCategoryBinding.inflate(LayoutInflater.from(parent?.context), parent, false)

        return DataViewHolder(viewBinding)
    }

}