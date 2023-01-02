package com.adam.submissionstoryapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.adam.submissionstoryapp.R
import com.adam.submissionstoryapp.UserPreference
import com.adam.submissionstoryapp.ui.auth.AuthenticationActivity
import com.adam.submissionstoryapp.ui.home.HomeActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val userPreference = UserPreference(this)

        Handler(Looper.getMainLooper()).postDelayed({
            if(userPreference.getUser().userId.toString().isNotEmpty()){
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                val intent = Intent(this, AuthenticationActivity::class.java)
                startActivity(intent)
                finish()
            }
        },100)
    }
}