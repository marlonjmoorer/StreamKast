package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.LiveData
import com.example.marlonmoorer.streamkast.data.PlaybackHistory

class LibraryViewModel:BaseViewModel(){



    fun getPlayBackHistory(): LiveData<List<PlaybackHistory>> {
        return repository.history.all
    }
}