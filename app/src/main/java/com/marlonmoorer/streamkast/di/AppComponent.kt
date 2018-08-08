package com.marlonmoorer.streamkast.di


import com.marlonmoorer.streamkast.ui.viewModels.BaseViewModel
import com.marlonmoorer.streamkast.ui.viewModels.LibraryViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(AppModule::class))
@Singleton
interface AppComponent {

    fun inject(viewModel: BaseViewModel)
    fun inject(viewModel: LibraryViewModel)
}