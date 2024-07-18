package com.hit.gymtime.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hit.gymtime.R

class MainActivity : AppCompatActivity() {

    //late init var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}