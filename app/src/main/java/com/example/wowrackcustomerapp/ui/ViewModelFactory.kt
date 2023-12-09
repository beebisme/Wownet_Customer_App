package com.example.wowrackcustomerapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wowrackcustomerapp.data.injection.Injection
import com.example.wowrackcustomerapp.data.repository.UserRepository
import com.example.wowrackcustomerapp.ui.auth.login.LoginViewModel
import com.example.wowrackcustomerapp.ui.auth.otp.OneTimePassViewModel
import com.example.wowrackcustomerapp.ui.main.section.article.NewsViewModel
import com.example.wowrackcustomerapp.ui.main.section.detail.DetailArticleViewModel
import com.example.wowrackcustomerapp.ui.main.section.home.HomeViewModel
import com.example.wowrackcustomerapp.ui.main.section.help.HelpViewModel
import com.example.wowrackcustomerapp.ui.main.section.hotspot.HotspotViewModel
import com.example.wowrackcustomerapp.ui.main.section.profile.ProfileViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HelpViewModel::class.java) -> {
                HelpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(OneTimePassViewModel::class.java) -> {
                OneTimePassViewModel(repository) as T
            }
            modelClass.isAssignableFrom(NewsViewModel::class.java) -> {
                NewsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailArticleViewModel::class.java) -> {
                DetailArticleViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HotspotViewModel::class.java) -> {
                HotspotViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                val userRepository = Injection.provideRepository(context)
                INSTANCE = ViewModelFactory(userRepository)
                INSTANCE as ViewModelFactory
            }
        }
        @JvmStatic
        fun clearInstance(){
            INSTANCE = null
        }
    }
}
