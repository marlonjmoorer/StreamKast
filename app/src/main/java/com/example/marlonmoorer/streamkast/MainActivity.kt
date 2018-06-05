package com.example.marlonmoorer.streamkast

import android.arch.lifecycle.Observer
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.marlonmoorer.streamkast.fragments.BrowseFragment
import com.example.marlonmoorer.streamkast.fragments.DetailFragment
import com.example.marlonmoorer.streamkast.fragments.EpisodeFragment
import com.example.marlonmoorer.streamkast.fragments.SectionFragment
import com.example.marlonmoorer.streamkast.viewModels.BrowseViewModel
import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel
import com.example.marlonmoorer.streamkast.viewModels.MediaPlayerViewModel
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {


    private var browseViewModel: BrowseViewModel?=null
    private var detailViewModel: DetailViewModel?=null
    private var service:MediaService?=null
    private val isBound
        get() = service!=null
    private val serviceConnection= object:ServiceConnection{
        override fun onServiceConnected(className: ComponentName?, binder: IBinder?) {
            if (binder is MediaService.MediaBinder){
                service=binder.getService()
            }
        }

        override fun onServiceDisconnected(className: ComponentName?) {
           service=null
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        browseViewModel = createViewModel()
        detailViewModel = createViewModel()

        navigation.setOnNavigationItemReselectedListener { item ->
            val fragment = when(item.itemId){
                R.id.menu_home-> BrowseFragment()
                else->null
            }
            fragment?.let { this.loadFragment(it) }
        }
        browseViewModel?.selectedPodcastId?.observe(this, Observer { id->
            val fragment = DetailFragment.newInstance(id!!)
            this.loadFragment(fragment)
        })
        browseViewModel?.selectedGenre?.observe(this, Observer {genre->
            val sectionFragment  =SectionFragment.newInstance(genre!!.id)
            this.loadFragment(sectionFragment)
        })
        detailViewModel?.selectedEpisode?.observe(this, Observer {episode->
            EpisodeFragment.newInstance(episode!!).show(supportFragmentManager,"")
        })

        detailViewModel?.queuedEpisode?.observe(this, Observer {episode->
            episode?.let {
                val intent=Intent(this,MediaService::class.java)
                bindService(intent,serviceConnection, BIND_AUTO_CREATE)
                service?.start()
            }
        })
        this.loadFragment(BrowseFragment())
    }

     fun loadFragment(fragment: Fragment)= supportFragmentManager!!
             .beginTransaction()
            .add(R.id.main,fragment)
            .addToBackStack("over")
            .commit()



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
}
