package com.example.marlonmoorer.streamkast.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.example.marlonmoorer.streamkast.R
import android.content.Intent

import com.obsez.android.lib.filechooser.ChooserDialog







class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
       when(key){
           "wifi_only"-> {
              var x= sharedPreferences?.getBoolean(key,true)
           }
       }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
         val chooser=ChooserDialog().with(context)
                .withFilter(true, false)
                .withStartFile(null)
                .withChosenListener { path, pathFile ->
                    val editor=preferenceScreen.sharedPreferences.edit()
                    editor.putString("location",path).apply()
                    findPreference("folderPicker")?.summary=path
                }
                .build()
        findPreference("folderPicker")?.run {
            summary=preferenceScreen.sharedPreferences.getString("location","")
            setOnPreferenceClickListener {
            chooser.show()
            return@setOnPreferenceClickListener true
            }
        }

    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        val path= data.data?.path
        var name= data.data?.toString()

        val editor=preferenceScreen.sharedPreferences.edit()
        editor.putString("location",path).apply()
    }



}
