package com.example.marlonmoorer.streamkast

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomSheetBehavior
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
import kotlinx.android.synthetic.main.item_podcast.*
import org.jetbrains.anko.intentFor


class MainActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener{


    private var browseViewModel: BrowseViewModel?=null
    private var detailViewModel: DetailViewModel?=null
    private var subscriptionViewModel:SubscriptionViewModel?=null
    private val targetId:Int=R.id.main
    private var searchFragment:SearchFragment?=null
    private var mediaPlayerFragment:MediaPlayerFragment?=null
    companion object {
        val PLAY_MEDIA="PLAY_MEDIA"
    }


    private var behavior: BottomSheetBehavior<View>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        browseViewModel = createViewModel()
        detailViewModel = createViewModel()
        subscriptionViewModel= createViewModel()
        nav.setOnNavigationItemSelectedListener(this)
        behavior=BottomSheetBehavior.from(findViewById<View>(R.id.bottom_sheet))?.apply {
            isHideable=true
            state=BottomSheetBehavior.STATE_HIDDEN
            setBottomSheetCallback(object :BottomSheetBehavior.BottomSheetCallback(){
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    nav.animate()
                            .translationY(slideOffset*nav.height)
                            .setDuration(0)
                    mediaPlayerFragment?.fadeMiniPlayer(slideOffset)
                }
                override fun onStateChanged(bottomSheet: View, newState: Int) {

                }
            })

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
        detailViewModel?.queuedEpisode?.observe(this, Observer {episode->
            behavior?.run{
                state=BottomSheetBehavior.STATE_COLLAPSED
                isHideable=false
            }
        })
        nav.selectedItemId=R.id.menu_browse
        searchFragment=SearchFragment()
        mediaPlayerFragment=supportFragmentManager.findFragmentById(R.id.mini_player) as MediaPlayerFragment
    }

    override fun onNewIntent(intent: Intent?) {
        if(intent?.action==MediaService.MEDIAPLAYER){
            behavior?.run{
                state=BottomSheetBehavior.STATE_EXPANDED
                isHideable=false
            }
        }
    }
    val podcastObserver = Observer<String> { id->
        id?.let { val fragment = DetailFragment.newInstance(id)
            this.addFragment(targetId,fragment)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val fragment = when(item.itemId){
            R.id.menu_browse-> BrowseFragment()
            R.id.menu_search->searchFragment!!
            R.id.menu_library->SubscriptionFragment()
            R.id.menu_subs->SubscriptionFragment()
            else-> Fragment()
        }
        replaceFragment(targetId,fragment)
        return true
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
        val i=intentFor<MediaService>().apply {
            action= MediaService.ACTION_STOP
        }
        startService(i)
    }





}
