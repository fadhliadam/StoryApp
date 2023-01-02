package com.adam.submissionstoryapp.ui.auth.login

import androidx.lifecycle.ViewModel
import com.adam.submissionstoryapp.network.local.repository.StoryRepository

class LoginViewModel(private val repo : StoryRepository) : ViewModel() {

    fun setLogin(email: String, password: String) = repo.setLogin(email,password)
}