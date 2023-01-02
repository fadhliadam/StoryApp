package com.adam.submissionstoryapp.ui.addstory

import androidx.lifecycle.ViewModel
import com.adam.submissionstoryapp.network.local.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repo: StoryRepository): ViewModel() {

    fun addNewStory(
        imageMultipart: MultipartBody.Part,
        description: RequestBody,
        lat: Double,
        lon: Double
    ) = repo.addNewStory(imageMultipart,description, lat, lon)
}