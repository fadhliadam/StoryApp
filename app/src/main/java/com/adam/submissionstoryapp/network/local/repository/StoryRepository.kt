package com.adam.submissionstoryapp.network.local.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.adam.submissionstoryapp.utils.Result
import com.adam.submissionstoryapp.UserPreference
import com.adam.submissionstoryapp.data.StoryRemoteMediator
import com.adam.submissionstoryapp.network.*
import com.adam.submissionstoryapp.network.local.database.StoryDatabase
import com.adam.submissionstoryapp.network.responses.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService, context: Context){

    private val token = "Bearer ${UserPreference(context).getUser().token}"

    fun getAllStoriesWithLocation(): LiveData<Result<GetAllStoriesResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllStories(
                token, size = 35, location = 1
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getAllStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 6
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    fun setRegister(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(
                name,
                email,
                password
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            val errorMessage = e.message.toString()
            emit(Result.Error(errorMessage))
        }
    }

    fun setLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(
                email,
                password
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            val errorMessage = e.message.toString()
            emit(Result.Error(errorMessage))
        }
    }

    fun addNewStory(
        imageMultipart: MultipartBody.Part,
        description: RequestBody,
        lat: Double,
        lon: Double,
    ): LiveData<Result<AddNewStoryResponse>> = liveData{
        emit(Result.Loading)
        try {
            val response = apiService.uploadImage(
                token,
                imageMultipart,
                description,
                lat,
                lon
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            val errorMessage = e.message.toString()
            emit(Result.Error(errorMessage))
        }
    }
}