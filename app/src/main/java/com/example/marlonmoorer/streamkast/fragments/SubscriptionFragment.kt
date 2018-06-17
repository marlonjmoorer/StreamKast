package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.App
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.adapters.SubscriptionAdapater
import com.example.marlonmoorer.streamkast.async
import com.example.marlonmoorer.streamkast.data.KastDatabase
import com.example.marlonmoorer.streamkast.data.Subscription
import kotlinx.android.synthetic.main.fragment_subscription.view.*
import javax.inject.Inject

class SubscriptionFragment:Fragment() {

    @Inject
    lateinit var database:KastDatabase

    lateinit var adapter:SubscriptionAdapater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.component?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        adapter= SubscriptionAdapater()
        return inflater.inflate(R.layout.fragment_subscription,container,false).apply {
            subs.layoutManager=GridLayoutManager(activity,2)
            subs.adapter=adapter
            val observer=Observer<List<Subscription>>{subscriptions->
                subscriptions?.let {
                     adapter.setSubList(subscriptions)
                }
            }
            async {
                database.SubscriptionDao().all.observe(this@SubscriptionFragment,observer)
            }


        }
    }

}