package com.hit.gymtime.ui


import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.hit.gymtime.R


class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {

        applyTheme();

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this);
    }

    private fun applyTheme() {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val theme = prefs.getString("theme_preference", "light")

        when (theme) {
            "light" -> setTheme(R.style.Theme_MyApp_Light)
            "dark" -> setTheme(R.style.Theme_MyApp_Dark)
        }
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        if (p1.equals("theme_preference")){
            recreate()
        }
    }
}