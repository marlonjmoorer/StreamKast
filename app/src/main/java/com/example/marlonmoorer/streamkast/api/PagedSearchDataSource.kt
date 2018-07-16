package com.example.marlonmoorer.streamkast.api

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.PageKeyedDataSource
import android.arch.paging.PositionalDataSource
import com.example.marlonmoorer.streamkast.api.models.Podcast
import com.example.marlonmoorer.streamkast.api.models.SearchResults
import com.example.marlonmoorer.streamkast.onResponse
import retrofit2.Call


class PagedSearchDataSource(private val query:String,private val repository: Repository): PositionalDataSource<Podcast>(){

    private var totalCount:Int=1000

    private fun loadSearchResults(position:Int, loadSize:Int): Call<SearchResults> {
        val searchQuery= mutableMapOf(
                "term" to query,
                "entity" to "podcast",
                "offset" to "$position",
                "limit" to "$loadSize"
        )
       return repository.search(searchQuery)
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Podcast>) {
        val position= computeInitialLoadPosition(params,totalCount)
        val loadSize = PositionalDataSource.computeInitialLoadSize(params, position, totalCount)
        loadSearchResults(position, loadSize).onResponse{result->
            result.results?.let {
                if(it.size <loadSize) totalCount= it.size
                callback.onResult(it, position, totalCount)
            }
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Podcast>) {
       loadSearchResults(params.startPosition,params.loadSize).onResponse {result->
           result.results?.let{
               callback.onResult(it)
           }
       }
    }

}