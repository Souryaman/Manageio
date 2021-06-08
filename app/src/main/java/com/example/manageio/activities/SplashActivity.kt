package com.example.manageio.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.manageio.R
import com.example.manageio.firebase.FirestoreClass

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()
        @Suppress("DEPRECATION")
        Handler().postDelayed({

            var currentUserId = FirestoreClass().getCurrentUserId()
            if(currentUserId.isNotEmpty())
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            else{
                startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
            }
            finish()
        },3000)

    }
}