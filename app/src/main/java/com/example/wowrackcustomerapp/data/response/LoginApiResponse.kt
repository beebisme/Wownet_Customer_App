package com.example.wowrackcustomerapp.data.response

import com.google.gson.annotations.SerializedName

data class LoginApiResponse(

	@field:SerializedName("data")
	val data: Data?,

	@field:SerializedName("error")
	var error: Error?
)

data class Data(

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("token")
	val token: String
)

data class Error(
	@field:SerializedName("message")
	val message: String
)

sealed class LoginResult {
	data object Loading : LoginResult()
	data class Success(val data: LoginApiResponse?) : LoginResult()
	data class Error(val message: String) : LoginResult()
}
