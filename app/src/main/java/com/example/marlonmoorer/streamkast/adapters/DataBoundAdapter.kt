package com.example.marlonmoorer.streamkast.adapters

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.marlonmoorer.streamkast.databinding.ItemEpisodeBinding

/**
 * Created by marlonmoorer on 3/30/18.
 */
abstract class DataBoundAdapter<T>:RecyclerView.Adapter<DataViewHolder<T>>() where T:ViewDataBinding {

    var listener:View.OnClickListener?=null
    
}