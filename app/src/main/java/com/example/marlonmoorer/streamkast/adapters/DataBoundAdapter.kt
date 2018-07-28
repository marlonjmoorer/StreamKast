package com.example.marlonmoorer.streamkast.adapters

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by marlonmoorer on 3/30/18.
 */


abstract class EditableAdapter<T>:RecyclerView.Adapter<T>() where T:RecyclerView.ViewHolder {

    protected  val EDITMODE=2
    protected  val VIEWMODE=1
    protected var editMode=false


    open fun setEditeMode(canEdit:Boolean){
        editMode=canEdit
        notifyDataSetChanged()
    }
    override fun getItemViewType(position: Int): Int {
        return if (editMode) EDITMODE else VIEWMODE
    }


}