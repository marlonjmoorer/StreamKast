package com.example.marlonmoorer.streamkast.ui.fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.models.IEpisode
import com.example.marlonmoorer.streamkast.ui.activities.FragmentEvenListener
import com.example.marlonmoorer.streamkast.ui.adapters.DownloadListAdapter
import com.example.marlonmoorer.streamkast.ui.adapters.EditableAdapter
import com.example.marlonmoorer.streamkast.ui.viewModels.LibraryViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.support.v4.alert

class DownloadListFragment: BaseFragment(), ActionMode.Callback, EditableAdapter.EditAdapterCallback {


    private lateinit var episodeAdapter: DownloadListAdapter

    private lateinit var viewModel: LibraryViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel=createViewModel()
        viewModel.downloadChangeEvent
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    episodeAdapter.update(it)
                }

        viewModel.getDownloaded().observe(this, Observer {downloads->
            downloads?.let {episodeAdapter.setEpisodes(it) }
        })

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        episodeAdapter= DownloadListAdapter(this)
        setHasOptionsMenu(true)
        return RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = episodeAdapter
        }
    }


    override fun onOpen(episode: IEpisode) {
        listener?.viewEpisode(episode)
    }

    override fun onDelete(episode: IEpisode) {
        viewModel.removeDownload(episode.guid)
    }

    override fun onLongClick(episode: IEpisode) {
        view?.startActionMode(this)
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_delete ->  alert("Delete these items?") {
                positiveButton("Delete"){
                    episodeAdapter.commitDeletion()
                    mode?.finish()
                }
                cancelButton {

                }
            }.show()
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