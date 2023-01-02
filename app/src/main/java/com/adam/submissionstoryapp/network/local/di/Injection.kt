package com.adam.submissionstoryapp.network.local.di

import android.content.Context
import com.adam.submissionstoryapp.network.ApiConfig
import com.adam.submissionstoryapp.network.local.database.StoryDatabase
import com.adam.submissionstoryapp.network.local.repository.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService,context)
    }
}