package com.praveen.basicimageeditor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.praveen.basicimageeditor.fragment.HomeFragment

class MainActivity : AppCompatActivity() {
    private val mTag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initHomeFragment()
    }

    private fun initHomeFragment() {
        try {
            val homeFragment = HomeFragment()
            supportFragmentManager.beginTransaction().replace(R.id.container,homeFragment).addToBackStack(null).commit()
        } catch (e: Exception) {
            Log.d(mTag,"initHomeFragment${e.printStackTrace()}")
        }
    }
}