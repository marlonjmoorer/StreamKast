package com.example.marlonmoorer.streamkast.ui.fragments

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.example.marlonmoorer.streamkast.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

}
