package com.rifqipadisiliwangi.sismartpju.view.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.rifqipadisiliwangi.sismartpju.databinding.ActivitySplashScreenBinding
import com.rifqipadisiliwangi.sismartpju.view.auth.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashScreenBinding
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        getUser()
    }

    private fun getUser(){
        val isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            Handler().postDelayed({
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
            }, 2000)
        } else {
            Handler().postDelayed({

                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
            }, 2000)
        }
    }
}