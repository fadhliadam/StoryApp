package com.adam.submissionstoryapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adam.submissionstoryapp.network.local.di.Injection
import com.adam.submissionstoryapp.ui.addstory.AddStoryViewModel
import com.adam.submissionstoryapp.ui.auth.login.LoginViewModel
import com.adam.submissionstoryapp.ui.auth.register.RegisterViewModel
import com.adam.submissionstoryapp.ui.home.StoryViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(Injection.provideRepository(context)) as T
        }
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(Injection.provideRepository(context)) as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(Injection.provideRepository(context)) as T
        }
        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)){
            return AddStoryViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}