package com.example.marlonmoorer.streamkast.di


import android.arch.lifecycle.AndroidViewModel
import com.example.marlonmoorer.streamkast.viewModels.BaseViewModel
import com.example.marlonmoorer.streamkast.viewModels.LibraryViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(AppModule::class))
@Singleton
interface AppComponent {

    fun inject(viewModel: BaseViewModel)
    fun inject(viewModel: LibraryViewModel)
}