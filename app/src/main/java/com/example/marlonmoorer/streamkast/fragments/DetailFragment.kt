package com.example.marlonmoorer.streamkast.fragments


import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.marlonmoorer.streamkast.R

import com.example.marlonmoorer.streamkast.api.models.rss.Episode
import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.databinding.FragmentDetailsBinding
import com.example.marlonmoorer.streamkast.listeners.IEpisodeListener


import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert

import org.jetbrains.anko.yesButton
import org.jetbrains.anko.design.snackbar
/**
 * Created by marlonmoorer on 3/24/18.
 */
class DetailFragment: BaseFragment(){

    lateinit var detailModel: DetailViewModel
    lateinit var binding:FragmentDetailsBinding
    private  var Id:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            Id=it.getString(KEY)
        }
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
        detailModel.loadPodcast(Id).observe(this, Observer { channel->
            if(channel==null){
                binding.loadingScreen?.visibility=View.VISIBLE
            }else{
                binding.channel=channel
                binding.loadingScreen?.visibility=View.GONE
                binding.executePendingBindings()
            }
        })

        binding.viewPager.adapter= ViewPagerAdapter(childFragmentManager)
        binding.tabs.run {
            setupWithViewPager(binding.viewPager)
            getTabAt(0)?.setIcon(R.drawable.icons8_bulleted_list_filled)
            getTabAt(1)?.setIcon(R.drawable.icons8_info_filled)
            return@run
        }
        detailModel.subscribed().observe(this, Observer {subscribed->

              subscribed?.let {
                  val button_text: String
                  val icon:Int
                  if(subscribed){
                      button_text=resources.getString(R.string.action_subbed)
                      icon=R.drawable.icons8_checked_user_male
                  }
                  else{
                      button_text=resources.getString(R.string.action_sub)
                      icon=R.drawable.icons8_add_user_male
                  }
                  binding.detailCard?.followBtn?.run {
                      text=button_text
                      setCompoundDrawablesWithIntrinsicBounds(0,0,icon,0)
                      setOnClickListener {
                          detailModel.toggleSubscription()
                      }
                  }
              }

        })
//        binding.detailCard?.followBtn?.setOnClickListener {
//            detailModel.run {
//                if(subbed==true){
//                    alert("Unsubcribe  from  $title ?") {
//                        yesButton {
//                            toggleSubscription()
//                            snackbar(binding.viewPager,"Unsubcribed from ${this@run.title} ","Undo") {
//                                toggleSubscription()
//                            }
//                        }
//                        noButton {}
//                    }.show()
//                }else{
//                    toggleSubscription()
//                }
//            }
//        }


        return binding.root
    }





    override fun onDestroyView() {
        super.onDestroyView()
        detailModel.getPodcast().removeObservers(this)
        detailModel.getEpisodes().removeObservers(this)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        detailModel = createViewModel()

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

