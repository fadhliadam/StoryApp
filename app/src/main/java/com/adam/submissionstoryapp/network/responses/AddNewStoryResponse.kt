package com.adam.submissionstoryapp.network.responses

import com.google.gson.annotations.SerializedName

data class AddNewStoryResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
