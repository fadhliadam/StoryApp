package com.adam.submissionstoryapp.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adam.submissionstoryapp.R

class AuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        supportActionBar?.hide()
    }
}