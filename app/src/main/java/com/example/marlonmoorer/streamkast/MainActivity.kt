package com.example.marlonmoorer.streamkast

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import com.example.marlonmoorer.streamkast.data.KastDatabase
import com.example.marlonmoorer.streamkast.fragments.*
import com.example.marlonmoorer.streamkast.viewModels.BrowseViewModel
import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel
import com.example.marlonmoorer.streamkast.viewModels.SubscriptionViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startService
import javax.inject.Inject


class MainActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener {


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
        navigation.setOnNavigationItemSelectedListener(this)

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
        navigation.selectedItemId=R.id.menu_browse
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
            action= MediaService.STOP
        }
        startService(i)
    }


}
