package com.adam.submissionstoryapp.network

import com.adam.submissionstoryapp.network.responses.AddNewStoryResponse
import com.adam.submissionstoryapp.network.responses.GetAllStoriesResponse
import com.adam.submissionstoryapp.network.responses.LoginResponse
import com.adam.submissionstoryapp.network.responses.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ) : GetAllStoriesResponse

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Double,
        @Part("lon") lon: Double,
    ): AddNewStoryResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse
}