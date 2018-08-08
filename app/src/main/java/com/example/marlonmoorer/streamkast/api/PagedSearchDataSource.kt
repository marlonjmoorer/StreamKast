package com.example.marlonmoorer.streamkast.api

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PositionalDataSource
import com.bumptech.glide.load.model.ModelLoader
import com.example.marlonmoorer.streamkast.SearchState
import com.example.marlonmoorer.streamkast.api.models.Podcast
import com.example.marlonmoorer.streamkast.api.models.SearchResults
import com.example.marlonmoorer.streamkast.onResponse
import retrofit2.Call


class PagedSearchDataSource(private val loadData: (Int,Int)->Call<SearchResults>): PositionalDataSource<Podcast>(){

    private var totalCount:Int=1000


    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Podcast>) {
        val position= computeInitialLoadPosition(params,totalCount)
        val loadSize = PositionalDataSource.computeInitialLoadSize(params, position, totalCount)
        loadData(position, loadSize).onResponse{result->
            result.results?.let {
                if(it.size <loadSize) totalCount= it.size
                callback.onResult(it, position, totalCount)
            }
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Podcast>) {
       loadData(params.startPosition,params.loadSize).onResponse {result->
           result.results?.let{
               callback.onResult(it)
           }
       }
    }

}