package com.marlonmoorer.streamkast.ui.fragments


import android.arch.lifecycle.Observer
import android.databinding.ObservableBoolean
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.marlonmoorer.streamkast.R

import com.marlonmoorer.streamkast.createViewModel
import com.marlonmoorer.streamkast.databinding.FragmentDetailsBinding
import com.marlonmoorer.streamkast.setIcon


import com.marlonmoorer.streamkast.ui.viewModels.DetailViewModel
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.support.v4.alert

/**
 * Created by marlonmoorer on 3/24/18.
 */
class DetailFragment:BaseFragment(){

    lateinit var detailModel: DetailViewModel
    lateinit var binding:FragmentDetailsBinding
    private  var Id:String=""
    private var loading= ObservableBoolean(true)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            Id=it.getString(KEY)
        }
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding =FragmentDetailsBinding.inflate(inflater,container,false)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeButtonEnabled(true)
            }
            binding.toolbar.setNavigationOnClickListener {
                this.onBackPressed()
            }
        }
        binding.viewPager.adapter= ViewPagerAdapter(childFragmentManager)
        binding.tabs.run {
            setupWithViewPager(binding.viewPager)
            getTabAt(0)?.setIcon(R.drawable.icons8_bulleted_list_filled)
            getTabAt(1)?.setIcon(R.drawable.icons8_info_filled)
            return@run
        }
        return binding.root
    }
    private fun setUpButton(subscribed:Boolean){
        binding.detailCard?.followBtn?.run {
            if(subscribed){
                text=getString(R.string.action_subbed)
                setIcon(R.drawable.icons8_check_mark_symbol)
                background=resources.getDrawable(R.drawable.button_subbed,activity?.theme)
            }
            else{
                text=getString(R.string.action_sub)
                setIcon(R.drawable.icons8_add)
                background=resources.getDrawable(R.drawable.button_unsubbed,activity?.theme)
            }
            setOnClickListener{
                toggleSubscription()
            }
        }
    }
    private fun toggleSubscription(){
         if(detailModel.isSubbed){

             alert(getString(R.string.unsub_format,detailModel.title)) {
                 positiveButton(getString(R.string.unsub_btn_text)){
                     detailModel.toggleSubscription()
                 }
                 cancelButton {}

             }.show()
         }else{
             detailModel.toggleSubscription()
         }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        detailModel = createViewModel()
        detailModel.loadPodcast(Id)
        binding.loading=loading
        detailModel.getPodcast().observe(this, Observer {podast->
            binding.podcast=podast
        })
        detailModel.podcastDetails.observe(this, Observer {details->
            if(details==null){
                loading.set(true)
            }else{
                binding.channel=details
                loading.set(false)
            }
            binding.executePendingBindings()
        })

        detailModel.subscribed.observe(this, Observer {subscribed->
            subscribed?.let(this::setUpButton)
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }

    override fun onStop() {
        super.onStop()
        detailModel.podcastDetails.removeObservers(this)
        detailModel.episodes.removeObservers(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }


    companion object {
        private const val KEY = "PODCAST_ID"
        @JvmStatic
        fun newInstance(podcastId:String) =
                DetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(KEY,podcastId)
                    }
                }
    }

    class ViewPagerAdapter(manager:FragmentManager): FragmentPagerAdapter(manager) {
        private var fragments: Array<Fragment>
        init {
            fragments= arrayOf(EpisodeListFragment(),InfoFragment())
        }
        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }
        override fun getCount()=fragments.size
    }


}

