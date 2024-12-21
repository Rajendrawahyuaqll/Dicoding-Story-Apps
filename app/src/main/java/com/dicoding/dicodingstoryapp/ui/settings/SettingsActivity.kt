package com.dicoding.dicodingstoryapp.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.dicodingstoryapp.databinding.ActivitySettingsBinding
import com.dicoding.dicodingstoryapp.ui.start.StartActivity
import com.dicoding.dicodingstoryapp.utils.SessionManager

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                SessionManager.clearSession(this)
                Toast.makeText(this, "Logout successfull!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, StartActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }

}
