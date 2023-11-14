package com.example.wowrackcustomerapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.wowrackcustomerapp.data.model.UserModel
import com.example.wowrackcustomerapp.data.repository.UserRepository

class MainViewModel(private val repository: UserRepository): ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}