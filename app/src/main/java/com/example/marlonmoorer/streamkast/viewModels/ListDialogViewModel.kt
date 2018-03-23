package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.marlonmoorer.streamkast.api.ItunesRepository
import com.example.marlonmoorer.streamkast.api.models.Genre
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.api.models.MediaItem
import com.example.marlonmoorer.streamkast.async
import java.util.*

/**
 * Created by marlonmoorer on 3/22/18.
 */
class ListDialogViewModel :ViewModel() {
    private var podcastList = MutableLiveData<List<MediaItem>>()
    private val loading=MutableLiveData<Boolean>()
    var itunesRepository: ItunesRepository
    init {
        itunesRepository= ItunesRepository()
    }

    val podcast
        get() = podcastList
    val isLoading
        get()=loading

    fun loadMore(genre: MediaGenre?)=async{
        loading.postValue(true)
        var podcast=genre?.let {
          itunesRepository.getShowsByGenre(genre,limit = 50)!!
        }?:run{
            itunesRepository.topPodCast(limit = 50)
        }
        loading.postValue(false)
        this.podcastList?.postValue(podcast)
    }




}