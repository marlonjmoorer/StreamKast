package com.example.marlonmoorer.streamkast

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.marlonmoorer.streamkast.fragments.BrowseFragment
import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.fragments.SectionFragment
import com.example.marlonmoorer.streamkast.viewModels.BrowseViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    private var browseViewModel: BrowseViewModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBar(appbar)
        browseViewModel = ViewModelProviders.of(this).get(BrowseViewModel::class.java!!)
        supportActionBar?.setDisplayHomeAsUpEnabled(false);
        navigation.setOnNavigationItemReselectedListener { item ->
           val fragment= when(item.itemId){
                R.id.menu_home-> BrowseFragment()
                else->null
            }
            this.loadFragment(fragment!!)
            return@setOnNavigationItemReselectedListener
        }
        browseViewModel?.selectedPodcast?.observe(this, Observer { podcast->

        })
        browseViewModel?.selectedGenre?.observe(this, Observer {genre->
            var sectionFragment  =SectionFragment.newInstance(genre!!.id!!)
            this.loadFragment(sectionFragment)
        })
        this.loadFragment(BrowseFragment())
    }

     fun loadFragment(fragment: Fragment){
        supportFragmentManager!!.beginTransaction()
//                .setCustomAnimations(
//                        R.anim.enter_right,
//                        R.anim.exit_right,
//                        R.anim.enter_right,
//                        R.anim.exit_right)/
        .add(R.id.container,fragment)
        .addToBackStack("over")
        .commit()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
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
