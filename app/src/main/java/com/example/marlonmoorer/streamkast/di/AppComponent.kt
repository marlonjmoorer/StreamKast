package com.example.marlonmoorer.streamkast.di


import com.example.marlonmoorer.streamkast.ui.viewModels.BaseViewModel
import com.example.marlonmoorer.streamkast.ui.viewModels.LibraryViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(AppModule::class))
@Singleton
interface AppComponent {

    fun inject(viewModel: BaseViewModel)
    fun inject(viewModel: LibraryViewModel)
}