package com.example.marlonmoorer.streamkast.ui.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.adapters.SubscriptionAdapater
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.data.Subscription
import com.example.marlonmoorer.streamkast.listeners.ISubscriptionListener
import com.example.marlonmoorer.streamkast.viewModels.SubscriptionViewModel
import kotlinx.android.synthetic.main.fragment_subscription.view.*

class SubscriptionFragment:BaseFragment(),ISubscriptionListener {


    lateinit var adapter:SubscriptionAdapater
    lateinit var viewModel:SubscriptionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        adapter= SubscriptionAdapater(this)
        return inflater.inflate(R.layout.fragment_subscription,container,false).apply {
            subs.layoutManager=GridLayoutManager(activity,2)
            subs.adapter=adapter
            (activity as AppCompatActivity).apply {
                setSupportActionBar(toolbar)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel=createViewModel()
        viewModel.subscriptions.observe(this, Observer {subs->
            subs?.let {
                adapter.setSubList(it)
            }
        })
    }

    override fun subscribe(sub: Subscription) =viewModel.subscribe(sub)
    override fun unsubscribe(sub: Subscription) =viewModel.unsubscribe(sub)
    override fun viewPodcast(id: String) =podcastListener.viewPodcast(id)


}