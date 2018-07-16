package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
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
    private var query:String=""
    private var factory:DataSource.Factory<Int,Podcast>
    private var searching:MutableLiveData<Boolean>


    val isSearchng:LiveData<Boolean>
        get() = searching

    init {

         pagedListConfig = PagedList.Config.Builder()
                 .setPageSize(10)
                 .build()
         executor = Executors.newFixedThreadPool(5)
         searching= MutableLiveData()
         factory=object:DataSource.Factory<Int,Podcast>(){
            override fun create(): DataSource<Int, Podcast> {
                return PagedSearchDataSource(query,repository)
            }
         }
         searchResults=LivePagedListBuilder(factory,pagedListConfig)
                .setFetchExecutor(executor).build()
         Transformations.map(searchResults,{results->
             if (searching.value==true){
                 searching.postValue(false)
             }
         })
    }


    private fun resetFactory(){
       searchResults.value?.dataSource?.invalidate()
    }

    fun searchForPodcast(searchTerm: String){
        query = searchTerm
        resetFactory()
        searching.postValue(true)
    }

}

