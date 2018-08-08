package com.marlonmoorer.streamkast.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.marlonmoorer.streamkast.ui.fragments.SettingsFragment

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .commit()
    }
}
