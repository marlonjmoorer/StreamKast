package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.databinding.ItemPodcastBinding
import android.icu.lang.UCharacter.GraphemeClusterBreak.V



/**
 * Created by marlonmoorer on 3/24/18.
 */
class DataViewHolder<T> (val binding:T):RecyclerView.ViewHolder(binding.root) where T:ViewDataBinding
