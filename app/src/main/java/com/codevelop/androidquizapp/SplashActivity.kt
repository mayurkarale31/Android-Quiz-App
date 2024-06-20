package com.codevelop.androidquizapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.codevelop.androidquizapp.databinding.SplashactivityBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding : SplashactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide the action bar in this activity
        supportActionBar?.hide()

        binding = SplashactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, UserInfoActivity::class.java)
            startActivity(intent)
            finish()
        },2000)
    }
}