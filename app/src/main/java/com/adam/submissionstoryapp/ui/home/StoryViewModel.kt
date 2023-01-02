package com.adam.submissionstoryapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.adam.submissionstoryapp.network.local.repository.StoryRepository
import com.adam.submissionstoryapp.network.responses.ListStoryItem

class StoryViewModel constructor(private var repo: StoryRepository): ViewModel(){

    private val _error  = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getAllStories(): LiveData<PagingData<ListStoryItem>> =
        repo.getAllStories().cachedIn(viewModelScope)

    fun getDataLocation() = repo.getAllStoriesWithLocation()
}
