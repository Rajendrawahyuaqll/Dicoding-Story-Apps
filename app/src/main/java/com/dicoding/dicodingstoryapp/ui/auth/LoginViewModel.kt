package com.dicoding.dicodingstoryapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingstoryapp.data.pref.UserModel
import com.dicoding.dicodingstoryapp.data.repository.StoryRepository
import com.dicoding.dicodingstoryapp.data.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: StoryRepository
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.login(email, password)
                if (response.error == false && response.loginResult != null) {
                    val userModel = UserModel(
                        email = email,
                        token = response.loginResult.token ?: "",
                        isLogin = true
                    )
                    repository.saveSession(userModel)
                    _loginResult.value = Result.success(response)
                } else {
                    _loginResult.value = Result.failure(Exception(response.message ?: "Login failed"))
                }
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}
