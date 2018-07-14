package com.example.marlonmoorer.streamkast.api

import android.arch.paging.PositionalDataSource
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.api.models.Podcast

class PagedCategoryDataSource (private val genre: MediaGenre, private val repository: Repository): PositionalDataSource<Podcast>(){

    var totalCount:Int=150
    private fun loadSearchResults(position:Int, loadSize:Int): List<Podcast> {
       // return repository.getShowsByGenre(genre)?: emptyList()
        return emptyList()
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Podcast>) {
        val position= computeInitialLoadPosition(params,totalCount)
        val loadSize = PositionalDataSource.computeInitialLoadSize(params, position, totalCount)
        callback.onResult(loadSearchResults(position, loadSize), position, totalCount)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Podcast>) {
        callback.onResult(loadSearchResults(params.startPosition,params.loadSize))
    }




}