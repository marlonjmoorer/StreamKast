package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.example.marlonmoorer.streamkast.App
import com.example.marlonmoorer.streamkast.api.Repository
import com.example.marlonmoorer.streamkast.data.KastDatabase
import javax.inject.Inject

abstract class BaseViewModel:ViewModel() {

    @Inject
    lateinit var repository: Repository

    class ViewModelFactory: ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val vm=modelClass.getConstructor().newInstance()
            if(vm is BaseViewModel){
                App.component?.inject(vm)
            }
            return vm
        }
    }

}

