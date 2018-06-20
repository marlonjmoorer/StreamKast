package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.adapters.SubscriptionAdapater
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.viewModels.SubscriptionViewModel
import kotlinx.android.synthetic.main.fragment_subscription.view.*

class SubscriptionFragment:Fragment() {


    lateinit var adapter:SubscriptionAdapater

    lateinit var viewModel:SubscriptionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=createViewModel()
        adapter= SubscriptionAdapater(viewModel)
        viewModel.subscriptions.observe(this, Observer {subs->
            subs?.let { adapter.setSubList(it) }
        })
    }




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_subscription,container,false).apply {
            subs.layoutManager=GridLayoutManager(activity,2)
            subs.adapter=adapter
        }
    }

}