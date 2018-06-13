package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.marlonmoorer.streamkast.api.Repository
import com.example.marlonmoorer.streamkast.api.models.SearchResults
import com.example.marlonmoorer.streamkast.async
import org.jetbrains.anko.doAsync

class SearchViewModel:BaseViewModel() {

    val _searchResults:MutableLiveData<SearchResults> = MutableLiveData()


    val searchResults:LiveData<SearchResults>
        get() = _searchResults

    fun getSearchResults(query:String):LiveData<SearchResults>{
        async {
            val results= repository.search(mapOf(
                    "term" to query,
                    "entity" to "podcast"
            ))
            _searchResults.postValue(results)
        }
        return searchResults
    }




}