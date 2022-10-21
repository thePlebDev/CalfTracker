package com.elliottsoftware.calftracker.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elliottsoftware.calftracker.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_main)

    }
}