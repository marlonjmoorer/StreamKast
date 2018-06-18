package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.App
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.adapters.SubscriptionAdapater
import com.example.marlonmoorer.streamkast.async
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.data.KastDatabase
import com.example.marlonmoorer.streamkast.data.Subscription
import com.example.marlonmoorer.streamkast.listeners.OnSubscriptionMenuListener
import com.example.marlonmoorer.streamkast.viewModels.SubscriptionViewModel
import kotlinx.android.synthetic.main.fragment_subscription.view.*
import javax.inject.Inject

class SubscriptionFragment:Fragment(),OnSubscriptionMenuListener {


    lateinit var adapter:SubscriptionAdapater

    lateinit var viewModel:SubscriptionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=createViewModel()
        adapter= SubscriptionAdapater(this)
        viewModel.subscriptions.observe(this, Observer {subs->
            subs?.let { adapter.setSubList(it) }
        })
    }

    override fun unSubscribe(sub:Subscription) {
        viewModel.unsubscribe(sub.podcastId!!)
        this.view?.let {
            Snackbar.make(it, "Unsubscribed", Snackbar.LENGTH_LONG)
            .setAction("Undo",{
              viewModel.subscribe(sub)
            })
            .show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_subscription,container,false).apply {
            subs.layoutManager=GridLayoutManager(activity,2)
            subs.adapter=adapter
        }
    }

}