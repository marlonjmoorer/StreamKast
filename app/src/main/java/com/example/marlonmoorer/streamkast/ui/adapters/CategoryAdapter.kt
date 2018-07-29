package com.example.marlonmoorer.streamkast.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.databinding.ItemCategoryBinding
import io.reactivex.subjects.PublishSubject

/**
 * Created by marlonmoorer on 4/2/18.
 */
class CategoryAdapter:RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    var categories=MediaGenre.categories

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories.get(position))
    }

    override fun getItemCount()= categories.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }

    val clickEvent= PublishSubject.create<MediaGenre>()

    inner class ViewHolder(val binding: ItemCategoryBinding):RecyclerView.ViewHolder(binding.root){

        init {
            binding.root.setOnClickListener {
                clickEvent.onNext(categories[layoutPosition])
            }
        }
        fun bind(model:MediaGenre){
            binding.genre=model
        }
    }
}