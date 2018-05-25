package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.ISelectHandler
import com.example.marlonmoorer.streamkast.databinding.ItemEpisodeBinding
import com.example.marlonmoorer.streamkast.databinding.ItemFeaturedTileBinding

/**
 * Created by marlonmoorer on 3/30/18.
 */
abstract class DataBoundAdapter<T>():RecyclerView.Adapter<DataViewHolder<T>>() where T:ViewDataBinding {

  var handler: ISelectHandler? = null



}