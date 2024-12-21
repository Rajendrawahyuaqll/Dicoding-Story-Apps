package com.dicoding.dicodingstoryapp.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.dicodingstoryapp.ViewModelFactory
import com.dicoding.dicodingstoryapp.ui.main.MainActivity
import com.dicoding.dicodingstoryapp.ui.start.StartActivity

class SplashActivity : AppCompatActivity() {
    private val viewModel by viewModels<SplashViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                navigateToMain()
            } else {
                navigateToStart()
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToStart() {
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
        finish()
    }
}
