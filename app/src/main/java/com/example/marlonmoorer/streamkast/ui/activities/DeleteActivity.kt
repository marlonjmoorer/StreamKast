package com.example.marlonmoorer.streamkast.ui.activities

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.adapters.DownloadListAdapter
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.viewModels.LibraryViewModel
import kotlinx.android.synthetic.main.activity_delete.*

class DeleteActivity : AppCompatActivity() {

    companion object {
        var AREA_KEY="area key"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete)
        val viewModel=createViewModel<LibraryViewModel>()
        val listAdapter=DownloadListAdapter()
        delete_list.apply {
            adapter=listAdapter
            layoutManager=LinearLayoutManager(context)
        }
//        viewModel.getDownloads().observe(this, Observer { downloads->
//            downloads?.let { listAdapter.setEpisodes(it) }
//        })

    }
}
