package com.example.wowrackcustomerapp.ui.main.section.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wowrackcustomerapp.data.repository.UserRepository
import kotlinx.coroutines.launch


class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    // Default no-argument constructor
//    constructor(application: Application) : this(application, UserRepository()) {
//        // You can pass a different UserRepository instance if needed
//    }

    // Your ViewModel code here

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
