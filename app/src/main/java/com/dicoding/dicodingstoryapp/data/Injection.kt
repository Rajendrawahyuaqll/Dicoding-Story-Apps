package com.dicoding.dicodingstoryapp.data

import android.content.Context
import com.dicoding.dicodingstoryapp.data.api.ApiConfig
import com.dicoding.dicodingstoryapp.data.database.StoryDatabase
import com.dicoding.dicodingstoryapp.data.pref.UserPreference
import com.dicoding.dicodingstoryapp.data.pref.dataStore
import com.dicoding.dicodingstoryapp.data.repository.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(context)
        return StoryRepository.getInstance(pref, apiService, database)
    }
}