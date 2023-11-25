package com.example.wowrackcustomerapp.ui.main.section.help

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.wowrackcustomerapp.data.models.UserModel
import com.example.wowrackcustomerapp.data.repository.UserRepository

class HelpViewModel(private val repository: UserRepository): ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}