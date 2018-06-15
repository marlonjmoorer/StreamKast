package com.example.marlonmoorer.streamkast.di


import android.support.v4.app.Fragment
import com.example.marlonmoorer.streamkast.fragments.SubscriptionFragment
import com.example.marlonmoorer.streamkast.viewModels.BaseViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(AppModule::class))
@Singleton
interface AppComponent {

    fun inject(view: BaseViewModel)
    fun inject(view: SubscriptionFragment)
}