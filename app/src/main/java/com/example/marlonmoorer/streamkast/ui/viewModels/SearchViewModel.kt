package com.example.marlonmoorer.streamkast.ui.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder

import android.arch.paging.PagedList
import com.example.marlonmoorer.streamkast.SearchState
import com.example.marlonmoorer.streamkast.api.PagedSearchDataSource
import com.example.marlonmoorer.streamkast.api.models.Podcast
import com.example.marlonmoorer.streamkast.api.models.SearchResults
import retrofit2.Call
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class SearchViewModel:BaseViewModel() {

    var searchResults:LiveData<PagedList<Podcast>>

    private var pagedListConfig: PagedList.Config
    private var executor:ExecutorService
    private var query:String=""
    private var factory:DataSource.Factory<Int,Podcast>



    init {

         pagedListConfig = PagedList.Config.Builder()
                 .setPageSize(10)
                 .build()
         executor = Executors.newFixedThreadPool(5)
         factory=object:DataSource.Factory<Int,Podcast>(){
            override fun create(): DataSource<Int, Podcast> {
                return PagedSearchDataSource(this@SearchViewModel::loadSearchResults)
            }
         }
         searchResults=LivePagedListBuilder(factory,pagedListConfig)
                .setFetchExecutor(executor).build()
    }


    private fun resetFactory(){
       searchResults.value?.dataSource?.invalidate()
    }

    private fun loadSearchResults(position:Int, loadSize:Int): Call<SearchResults> {
        val searchQuery= mutableMapOf(
                "term" to query,
                "entity" to "podcast",
                "offset" to "$position",
                "limit" to "$loadSize"
        )
        return repository.search(searchQuery)
    }

    fun searchForPodcast(searchTerm: String){
        query = searchTerm
        resetFactory()
    }

}

