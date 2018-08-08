package com.marlonmoorer.streamkast.ui.fragments

import android.arch.lifecycle.Observer

import android.os.Bundle

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.marlonmoorer.streamkast.R
import com.marlonmoorer.streamkast.createViewModel
import com.marlonmoorer.streamkast.models.IEpisode
import com.marlonmoorer.streamkast.ui.adapters.EditableAdapter
import com.marlonmoorer.streamkast.ui.adapters.HistoryListAdapter
import com.marlonmoorer.streamkast.ui.viewModels.LibraryViewModel
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.support.v4.alert

class PlaybackHistoryFragment: BaseFragment(), ActionMode.Callback, EditableAdapter.EditAdapterCallback {


    private lateinit var episodeAdapter: HistoryListAdapter

    private lateinit var viewModel: LibraryViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel=createViewModel()
        viewModel.getPlayBackHistory().observe(this, Observer { episodes->
            episodes?.let { episodeAdapter.setEpisodes(it) }
        })
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        episodeAdapter= HistoryListAdapter(this)
        return RecyclerView(context).apply{
            adapter=episodeAdapter
            layoutManager= LinearLayoutManager(context)
        }
    }


    override fun onOpen(episode: IEpisode) {
        listener?.viewEpisode(episode)
    }

    override fun onDelete(episode: IEpisode) {
       viewModel.removeHistory(episode.guid)
    }

    override fun onLongClick(episode: IEpisode) {
       view?.startActionMode(this)
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_delete ->{
                alert(getString(R.string.delete_download_message)) {
                    positiveButton(getString(R.string.delete_btn_text)){
                        episodeAdapter.commitDeletion()
                        mode?.finish()
                    }
                    cancelButton {

                    }
                }.show()
            }
        }
        return true
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.edit_menu,menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        (parentFragment as IModeChangeListener).onModeChange(true)
        episodeAdapter.setEditeMode(true)
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        (parentFragment as IModeChangeListener).onModeChange(false)
        episodeAdapter.setEditeMode(false)
    }

}