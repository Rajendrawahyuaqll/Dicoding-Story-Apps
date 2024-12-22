package com.dicoding.dicodingstoryapp.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.dicoding.dicodingstoryapp.data.api.ApiConfig
import com.dicoding.dicodingstoryapp.data.database.StoryDatabase
import com.dicoding.dicodingstoryapp.data.pref.UserPreference
import com.dicoding.dicodingstoryapp.data.repository.StoryRepository
import com.dicoding.dicodingstoryapp.databinding.ActivitySettingsBinding
import com.dicoding.dicodingstoryapp.ui.start.StartActivity
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var storyRepository: StoryRepository
    private val Context.dataStore by preferencesDataStore(name = "user_preferences")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userPreference = UserPreference.getInstance(dataStore)
        val apiService = ApiConfig.getApiService(this)
        val database = StoryDatabase.getDatabase(this)
        storyRepository = StoryRepository.getInstance(userPreference, apiService, database)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        setupAction()
    }

    private fun setupAction() {
        binding.btnLanguage.setOnClickListener {
            startActivity(Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.btnLogout.setOnClickListener {
            performLogout()
        }
    }

    private fun performLogout() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                logoutUser()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun logoutUser() {
        lifecycleScope.launch {
            try {
                storyRepository.logout()
                Toast.makeText(this@SettingsActivity, "Logout successful!", Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(this@SettingsActivity, StartActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                finish()
            } catch (e: Exception) {
                Toast.makeText(
                    this@SettingsActivity,
                    "Failed to logout: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
