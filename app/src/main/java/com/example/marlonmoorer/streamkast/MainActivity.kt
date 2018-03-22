package com.example.marlonmoorer.streamkast

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.marlonmoorer.streamkast.fragments.FeaturedFragment

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        navigation.setOnNavigationItemReselectedListener { item ->
           val fragament= when(item.itemId){
                R.id.menu_home-> FeaturedFragment()
                else->null
            }
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.container, fragament).commit();
            return@setOnNavigationItemReselectedListener
        }
        getSupportFragmentManager().beginTransaction().
                  replace(R.id.container, FeaturedFragment()).commit();
    }

    override fun onStart() {
        super.onStart()
        navigation.selectedItemId=R.id.menu_home
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
