package com.dicoding.dicodingstoryapp.ui.story

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingstoryapp.data.repository.StoryRepository
import com.dicoding.dicodingstoryapp.data.response.Story
import kotlinx.coroutines.launch

class DetailStoryViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _detailStory = MutableLiveData<Story?>()
    val detailStory: MutableLiveData<Story?> = _detailStory

    fun getStoryById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = storyRepository.getDetailStory(id)
                _detailStory.value = response.story
            } catch (e: Exception) {
                _detailStory.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}