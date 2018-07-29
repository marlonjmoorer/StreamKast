package com.example.marlonmoorer.streamkast.ui.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.*
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.ui.adapters.SubscriptionAdapater
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.data.Subscription
import com.example.marlonmoorer.streamkast.ui.viewModels.SubscriptionViewModel
import kotlinx.android.synthetic.main.fragment_subscription.view.*
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.contentView
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.support.v4.alert

class SubscriptionFragment: BaseFragment(),SubscriptionAdapater.SubscriptionAdapterCallback,ActionMode.Callback{


    lateinit var adapter:SubscriptionAdapater
    lateinit var viewModel:SubscriptionViewModel
    private  var isInActionMode:Boolean=false



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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.library_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_edit->{
                view?.startActionMode(this)
            }
        }
        return false
    }

    override fun onOpen(id: String) {
        listener?.viewPodcast(id)
    }

    override fun onUnsubscribe(subscription: Subscription) {
        viewModel.unsubscribe(subscription)
        if(!isInActionMode)
            activity?.contentView?.let {
                snackbar(it,"Unsubscribed","Undo"){
                     viewModel.subscribe(subscription)
                }
            }
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_unsubscribe->{
                alert("Usubscribe from  these podcast?") {
                    positiveButton("Unsubscribe"){
                       adapter.commitUnsinscribe()
                        mode?.finish()
                    }
                    cancelButton {

                    }
                }.show()
            }
        }
        return true
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.menu_subscriptions,menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        adapter.setEditeMode(true)
        isInActionMode=true
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        adapter.setEditeMode(false)
        isInActionMode=false
    }
}