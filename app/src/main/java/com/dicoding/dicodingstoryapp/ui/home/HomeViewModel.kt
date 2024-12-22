package com.dicoding.dicodingstoryapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.dicoding.dicodingstoryapp.data.repository.StoryRepository
import com.dicoding.dicodingstoryapp.data.response.ListStoryItem
import kotlinx.coroutines.launch

class HomeViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _listStory = MutableLiveData<PagingData<ListStoryItem>>()
    val listStory: MutableLiveData<PagingData<ListStoryItem>> = _listStory

    fun getStoriesWithToken() {
        viewModelScope.launch {
            storyRepository.getSession().collect {
                fetchListStory()
            }
        }
    }

    private fun fetchListStory() {
        _isLoading.value = true
        viewModelScope.launch {
            storyRepository.getStories().observeForever { pagingData ->
                _listStory.value = pagingData
                _isLoading.value = false
            }
        }
    }
}