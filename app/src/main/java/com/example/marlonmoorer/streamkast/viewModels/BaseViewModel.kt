package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.ViewModel
import com.example.marlonmoorer.streamkast.App
import com.example.marlonmoorer.streamkast.api.Repository
import com.example.marlonmoorer.streamkast.data.KastDatabase
import javax.inject.Inject

abstract class BaseViewModel:ViewModel() {

    @Inject
    lateinit var database: KastDatabase

    @Inject
    lateinit var repository: Repository

    init {
        App.component?.inject(this)
    }

}
