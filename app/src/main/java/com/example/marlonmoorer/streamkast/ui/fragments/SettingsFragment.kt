package com.example.marlonmoorer.streamkast.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.example.marlonmoorer.streamkast.R
import android.content.Intent

import com.obsez.android.lib.filechooser.ChooserDialog







class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
         val chooser=ChooserDialog().with(context)
                .withFilter(true, false)
                .withStartFile(null)
                .withChosenListener { path, pathFile ->
                    val editor=preferenceScreen.sharedPreferences.edit()
                    editor.putString(getString(R.string.key_storage),path).apply()
                    findPreference("folderPicker")?.summary=path
                }
                .build()
        findPreference("folderPicker")?.run {
            summary=preferenceScreen.sharedPreferences.getString(getString(R.string.key_storage),"")
            setOnPreferenceClickListener {
                chooser.show()
                return@setOnPreferenceClickListener true
            }
        }

    }
    
}
