package com.dicoding.dicodingstoryapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingstoryapp.data.repository.StoryRepository
import com.dicoding.dicodingstoryapp.data.response.Story
import kotlinx.coroutines.launch

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _storiesWithLocation = MutableLiveData<List<Story>>()
    val storiesWithLocation: LiveData<List<Story>> = _storiesWithLocation

    fun fetchStoriesWithLocation() {
        viewModelScope.launch {
            try {
                val stories = storyRepository.getStoriesWithLocation()
                _storiesWithLocation.value = stories
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
