package com.example.wowrackcustomerapp.data.response

import com.google.gson.annotations.SerializedName

data class LoginOTPResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("error")
	val error: ErrorOTP
)
data class ErrorOTP(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)
