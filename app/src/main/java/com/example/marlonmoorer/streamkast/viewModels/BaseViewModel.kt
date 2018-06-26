package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.example.marlonmoorer.streamkast.App
import com.example.marlonmoorer.streamkast.api.Repository
import com.example.marlonmoorer.streamkast.data.KastDatabase
import javax.inject.Inject

abstract class BaseViewModel:ViewModel() {

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var context:Context

    class ViewModelFactory: ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass== BaseViewModel::class.java){
                val vm=modelClass.getConstructor().newInstance()
                App.component?.inject(vm)
                return vm as T
            }
            return modelClass.getConstructor().newInstance()
        }
    }

}

