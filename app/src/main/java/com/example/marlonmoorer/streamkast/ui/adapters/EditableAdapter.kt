package com.example.marlonmoorer.streamkast.ui.adapters

import android.support.v7.widget.RecyclerView
import com.example.marlonmoorer.streamkast.models.IEpisode

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

    interface EditAdapterCallback{
        fun onOpen(episode: IEpisode)

        fun onDelete(episode: IEpisode)

        fun onLongClick(episode: IEpisode)
    }


}