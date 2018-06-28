package com.example.marlonmoorer.streamkast

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.arch.lifecycle.Observer
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import com.example.marlonmoorer.streamkast.fragments.*
import com.example.marlonmoorer.streamkast.viewModels.BrowseViewModel
import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel
import com.example.marlonmoorer.streamkast.viewModels.SubscriptionViewModel
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import org.jetbrains.anko.find
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener, SlidingUpPanelLayout.PanelSlideListener {


    private var browseViewModel: BrowseViewModel?=null
    private var detailViewModel: DetailViewModel?=null
    private var subscriptionViewModel:SubscriptionViewModel?=null
    private val targetId:Int=R.id.main




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        browseViewModel = createViewModel()
        detailViewModel = createViewModel()
        subscriptionViewModel= createViewModel()
        nav.setOnNavigationItemSelectedListener(this)
        supportFragmentManager.findFragmentById(R.id.mini_player)?.run {
            if(this is SlidingUpPanelLayout.PanelSlideListener){
                val slidePanel = find<SlidingUpPanelLayout>(R.id.sliding_panel)
                slidePanel.addPanelSlideListener(this)
                slidePanel.addPanelSlideListener(this@MainActivity)
            }
        }


        browseViewModel?.getSelectedPodCastId()?.observe(this, podcastObserver)
        subscriptionViewModel?.selectedPodcastId?.observe(this,podcastObserver)

        browseViewModel?.getCurrentGenre()?.observe(this, Observer {genre->
           genre?.let {
               val sectionFragment = SectionFragment.newInstance(genre.id)
               this.addFragment(targetId,sectionFragment)
           }
        })
        detailViewModel?.getCurrentEpisode()?.observe(this, Observer {episode->
            EpisodeFragment().show(supportFragmentManager,"")
        })
        nav.selectedItemId=R.id.menu_browse


    }
    val podcastObserver = Observer<String> { id->
        id?.let { val fragment = DetailFragment.newInstance(id)
            this.addFragment(targetId,fragment)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val fragment = when(item.itemId){
            R.id.menu_browse-> BrowseFragment()
            R.id.menu_search->SearchFragment()
            R.id.menu_library->SubscriptionFragment()
            else-> Fragment()
        }
        replaceFragment(targetId,fragment)
        return true
    }
    override fun onPanelSlide(panel: View?, slideOffset: Float) {
        nav.animate()
        .translationY(slideOffset*nav.height)
        .setDuration(0)

    }

    override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
//        when(newState){
//           SlidingUpPanelLayout.PanelState.COLLAPSED-> nav.visibility=View.VISIBLE
//            SlidingUpPanelLayout.PanelState.EXPANDED->nav.visibility=View.GONE
//        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        val i=intentFor<MediaService>().apply {
//            action= MediaService.STOP
//        }
//        startService(i)
    }




}
