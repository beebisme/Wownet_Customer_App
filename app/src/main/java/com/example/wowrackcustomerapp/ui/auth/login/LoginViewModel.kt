package com.example.wowrackcustomerapp.ui.auth.login

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.wowrackcustomerapp.data.models.UserModel
import com.example.wowrackcustomerapp.data.repository.UserRepository
import com.example.wowrackcustomerapp.data.response.Error
import com.example.wowrackcustomerapp.data.response.LoginApiResponse
import com.example.wowrackcustomerapp.data.response.LoginResult
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<LoginApiResponse?>()
    val loginResult: MutableLiveData<LoginApiResponse?> get() = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
//            _loginResult.value = LoginResult.Loading

            try {
                val response = repository.login(email, password)
                response.enqueue(object :Callback<LoginApiResponse>{
                    override fun onResponse(
                        call: Call<LoginApiResponse>,
                        response: Response<LoginApiResponse>
                    ) {
                        val responseBody = response.body()
                        if (responseBody != null){
                            _loginResult.value = responseBody
                        }
                    }

                    override fun onFailure(call: Call<LoginApiResponse>, t: Throwable) {
                        _loginResult.value?.error = Error(t.message.toString())
                    }

                })
            } catch (e: Exception) {
                _loginResult.value?.error = Error(e.message.toString())
            }
        }
    }
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}