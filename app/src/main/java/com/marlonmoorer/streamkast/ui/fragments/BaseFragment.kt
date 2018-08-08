package com.marlonmoorer.streamkast.ui.fragments

import android.content.Context
import android.support.v4.app.Fragment
import com.marlonmoorer.streamkast.ui.activities.FragmentEvenListener


abstract class   BaseFragment: Fragment(){

    protected var listener: FragmentEvenListener?=null


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener= context as FragmentEvenListener
    }



}