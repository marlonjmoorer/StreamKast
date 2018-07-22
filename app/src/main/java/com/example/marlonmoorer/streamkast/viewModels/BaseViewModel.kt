package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.example.marlonmoorer.streamkast.App
import com.example.marlonmoorer.streamkast.api.Repository
import com.example.marlonmoorer.streamkast.data.KastDatabase
import com.tonyodev.fetch2.Fetch
import javax.inject.Inject

abstract class BaseViewModel:ViewModel() {

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var context:Context

    @Inject
    lateinit var  fetch: Fetch



    class ViewModelFactory: ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val vm=super.create(modelClass)
            if(vm is BaseViewModel){
                App.component?.inject(vm)
            }

            return vm
        }
    }

}

