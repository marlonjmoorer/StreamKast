package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by marlonmoorer on 3/30/18.
 */
abstract class DataBoundAdapter<T>():RecyclerView.Adapter<DataBoundAdapter.DataViewHolder<T>>() where T:ViewDataBinding {


    class DataViewHolder<T> (val binding:T):RecyclerView.ViewHolder(binding.root) where T:ViewDataBinding{

    }
}