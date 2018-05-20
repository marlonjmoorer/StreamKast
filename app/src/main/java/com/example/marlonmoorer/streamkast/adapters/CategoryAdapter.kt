package com.example.marlonmoorer.streamkast.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.databinding.ItemCategoryBinding
import com.example.marlonmoorer.streamkast.load

/**
 * Created by marlonmoorer on 4/2/18.
 */
class CategoryAdapter:DataBoundAdapter<ItemCategoryBinding>() {

    var categories=MediaGenre.values()
    override fun onBindViewHolder(holder: DataViewHolder<ItemCategoryBinding>?, position: Int) {
        holder!!.binding.categoryIcon.load(categories[position].imageId)
    }

    override fun getItemCount()= categories.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataViewHolder<ItemCategoryBinding> {
        val viewBinding = ItemCategoryBinding.inflate(LayoutInflater.from(parent?.context), parent, false)

        return DataViewHolder(viewBinding)
    }

}