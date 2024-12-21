package com.dicoding.dicodingstoryapp.utils

import android.content.Context

object SessionManager {
    fun clearSession(context: Context) {
        val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }
}
