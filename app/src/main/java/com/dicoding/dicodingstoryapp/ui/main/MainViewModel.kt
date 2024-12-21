package com.dicoding.dicodingstoryapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingstoryapp.data.repository.StoryRepository
import kotlinx.coroutines.launch

class MainViewModel (private val repository: StoryRepository):ViewModel(){

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}