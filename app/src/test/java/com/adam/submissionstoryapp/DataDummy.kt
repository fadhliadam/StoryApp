package com.adam.submissionstoryapp

import com.adam.submissionstoryapp.network.responses.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummy {

    fun generateDummyListStory(): List<ListStoryItem> {
        val listStory: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val storyItem = ListStoryItem(
                i.toString(),
                "$i second",
                "story $i",
                "description $i",
                i.toDouble(),
                i.toString(),
                i.toDouble()
            )
            listStory.add(storyItem)
        }
        return listStory
    }

    fun generateDummyStoriesResponse(): GetAllStoriesResponse {
        val error = false
        val message = "Stories fetched successfully"
        val listStory = mutableListOf<ListStoryItem>()

        for (i in 0 .. 100) {
            val storyItem = ListStoryItem(
                i.toString(),
                "$i second",
                "story $i",
                "description $i",
                i.toDouble(),
                i.toString(),
                i.toDouble()
            )
            listStory.add(storyItem)
        }

        return GetAllStoriesResponse(listStory, error, message)
    }

    fun generateDummyLoginResponse(): LoginResponse{
        val error = false
        val message = "success"

        val loginResult = LoginResult(
            "name",
            "userId",
            "token"
        )
        return LoginResponse(loginResult,error,message)
    }

    fun generateDummyRegisterResponse(): RegisterResponse{
        val error = false
        val message = "User Created"
        return RegisterResponse(error,message)
    }

    fun generateDummyUploadResponse(): AddNewStoryResponse{
        val error = false
        val message = "success"
        return AddNewStoryResponse(error,message)
    }

    fun generateDummyMultipartFile(): MultipartBody.Part {
        val dummyText = "text"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }

    fun generateDummyRequestBody(): RequestBody {
        val dummyText = "text"
        return dummyText.toRequestBody()
    }
}