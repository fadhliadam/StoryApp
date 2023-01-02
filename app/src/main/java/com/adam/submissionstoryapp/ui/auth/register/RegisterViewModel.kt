package com.adam.submissionstoryapp.ui.auth.register

import androidx.lifecycle.ViewModel
import com.adam.submissionstoryapp.network.local.repository.StoryRepository

class RegisterViewModel(private var repo: StoryRepository): ViewModel() {
    fun setRegister(name: String, email: String, password: String) =
        repo.setRegister(name, email, password)
}