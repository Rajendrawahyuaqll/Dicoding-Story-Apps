package com.dicoding.dicodingstoryapp.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.dicodingstoryapp.R
import com.dicoding.dicodingstoryapp.ViewModelFactory
import com.dicoding.dicodingstoryapp.databinding.ActivityMainBinding
import com.dicoding.dicodingstoryapp.ui.home.HomeFragment
import com.dicoding.dicodingstoryapp.utils.LogoutCallback

class MainActivity : AppCompatActivity(), LogoutCallback {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }
    }

    override fun onLogout() {
        viewModel.logout()
    }
}


