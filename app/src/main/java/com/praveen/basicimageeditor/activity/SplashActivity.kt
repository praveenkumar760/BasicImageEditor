package com.praveen.basicimageeditor.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.praveen.basicimageeditor.MainActivity
import com.praveen.basicimageeditor.R
import com.praveen.basicimageeditor.animation.CommonAnimation
import com.praveen.basicimageeditor.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var splashBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        init()
    }

    private fun init() {
        if (::splashBinding.isInitialized) {
            CommonAnimation.setAlphaAnimation(splashBinding.tvSplash, 0.1f, 1f, 2000L)
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 3500)
        }
    }
}