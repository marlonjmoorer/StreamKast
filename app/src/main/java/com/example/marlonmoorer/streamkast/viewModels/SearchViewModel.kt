package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import com.example.marlonmoorer.streamkast.api.Repository
import com.example.marlonmoorer.streamkast.api.models.SearchResults

import org.jetbrains.anko.doAsync
import android.arch.paging.PagedList
import com.example.marlonmoorer.streamkast.api.PagedSearchDataSource
import com.example.marlonmoorer.streamkast.api.models.Podcast
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class SearchViewModel:BaseViewModel() {

    var searchResults:LiveData<PagedList<Podcast>>
    private var pagedListConfig: PagedList.Config
    private var executor:ExecutorService

    init {
         pagedListConfig = PagedList.Config.Builder()
                 .setPageSize(10)
                 .build()
         executor = Executors.newFixedThreadPool(5)
         searchResults=MutableLiveData()
    }

    private fun createPagedList(query:String): LiveData<PagedList<Podcast>> {
        val searchQuery= mutableMapOf(
                "term" to query,
                "entity" to "podcast"
        )
        val factory=object:DataSource.Factory<Int,Podcast>(){
            override fun create(): DataSource<Int, Podcast> {
                return PagedSearchDataSource(searchQuery,repository)
            }
        }
        val builder=LivePagedListBuilder(factory,pagedListConfig)
        builder.setFetchExecutor(executor)
        return builder.build()
    }

    fun getPagesSearchResults(query: String): LiveData<PagedList<Podcast>> {
        searchResults = createPagedList(query)
        return searchResults
    }

}

