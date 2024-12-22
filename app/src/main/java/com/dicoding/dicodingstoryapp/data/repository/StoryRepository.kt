package com.dicoding.dicodingstoryapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.dicodingstoryapp.data.api.ApiService
import com.dicoding.dicodingstoryapp.data.database.StoryDatabase
import com.dicoding.dicodingstoryapp.data.pref.UserModel
import com.dicoding.dicodingstoryapp.data.pref.UserPreference
import com.dicoding.dicodingstoryapp.data.response.AddStoryResponse
import com.dicoding.dicodingstoryapp.data.response.DetailStoryResponse
import com.dicoding.dicodingstoryapp.data.response.ListStoryItem
import com.dicoding.dicodingstoryapp.data.response.LoginResponse
import com.dicoding.dicodingstoryapp.data.response.RegisterResponse
import com.dicoding.dicodingstoryapp.data.response.Story
import com.dicoding.dicodingstoryapp.utils.parseErrorMessage
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class StoryRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
    private val database: StoryDatabase
) {

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return try {
            val response = apiService.register(name, email, password)
            if (response.error == false) {
                response
            } else {
                throw Exception(response.message ?: "An unknown error occurred")
            }
        } catch (e: HttpException) {
            val errorMessage = parseErrorMessage(e)
            throw Exception(errorMessage)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = StoryRemoteMediator(database, apiService),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).liveData
    }

    suspend fun addStory(
        description: RequestBody,
        image: MultipartBody.Part,
        lat: Double?,
        lon: Double?
    ): AddStoryResponse {
        return try {
            apiService.addStory(description, image, lat, lon)
        } catch (e: HttpException) {
            val errorMessage = parseErrorMessage(e)
            throw Exception(errorMessage)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getStoriesWithLocation(): List<Story> {
        return try {
            val response = apiService.getStoriesWithLocation(location = 1)
            response.listStory?.map { item ->
                Story(
                    photoUrl = item?.photoUrl,
                    createdAt = item?.createdAt,
                    name = item?.name,
                    description = item?.description,
                    lon = item?.lon,
                    id = item?.id,
                    lat = item?.lat
                )
            } ?: emptyList()
        } catch (e: HttpException) {
            val errorMessage = parseErrorMessage(e)
            throw Exception(errorMessage)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getDetailStory(id: String): DetailStoryResponse {
        return try{
            apiService.getDetailStory(id)
        } catch (e: HttpException) {
            val errorMessage = parseErrorMessage(e)
            throw Exception(errorMessage)
        } catch (e: Exception) {
            throw e
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService,
            database: StoryDatabase
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(userPreference, apiService, database)
            }.also { instance = it }
    }
}