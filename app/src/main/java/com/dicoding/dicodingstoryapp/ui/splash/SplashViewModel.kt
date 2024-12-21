package com.dicoding.dicodingstoryapp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.dicodingstoryapp.data.pref.UserModel
import com.dicoding.dicodingstoryapp.data.repository.StoryRepository

class SplashViewModel (private val repository: StoryRepository): ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}