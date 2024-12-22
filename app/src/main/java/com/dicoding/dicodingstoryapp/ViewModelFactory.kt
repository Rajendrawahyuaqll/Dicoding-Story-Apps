package com.dicoding.dicodingstoryapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.dicodingstoryapp.data.Injection
import com.dicoding.dicodingstoryapp.data.repository.StoryRepository
import com.dicoding.dicodingstoryapp.ui.auth.LoginViewModel
import com.dicoding.dicodingstoryapp.ui.auth.RegisterViewModel
import com.dicoding.dicodingstoryapp.ui.home.HomeViewModel
import com.dicoding.dicodingstoryapp.ui.main.MainViewModel
import com.dicoding.dicodingstoryapp.ui.maps.MapsViewModel
import com.dicoding.dicodingstoryapp.ui.splash.SplashViewModel
import com.dicoding.dicodingstoryapp.ui.story.AddStoryViewModel
import com.dicoding.dicodingstoryapp.ui.story.DetailStoryViewModel

class ViewModelFactory(
    private val repository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailStoryViewModel::class.java) -> {
                DetailStoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(repository) as T
            }


            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    val repository = Injection.provideRepository(context)
                    INSTANCE = ViewModelFactory(repository)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}