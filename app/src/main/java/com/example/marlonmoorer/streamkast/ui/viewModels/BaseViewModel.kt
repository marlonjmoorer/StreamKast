package com.example.marlonmoorer.streamkast.ui.viewModels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.marlonmoorer.streamkast.App
import com.example.marlonmoorer.streamkast.api.Repository

import javax.inject.Inject

abstract class BaseViewModel:ViewModel() {

    @Inject
    lateinit var repository: Repository





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

