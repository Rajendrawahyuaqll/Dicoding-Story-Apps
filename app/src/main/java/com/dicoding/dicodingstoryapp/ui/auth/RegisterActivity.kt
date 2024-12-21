package com.dicoding.dicodingstoryapp.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.dicodingstoryapp.R
import com.dicoding.dicodingstoryapp.ViewModelFactory
import com.dicoding.dicodingstoryapp.databinding.ActivityRegisterBinding
import com.dicoding.dicodingstoryapp.utils.parseErrorMessage
import retrofit2.HttpException

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        setupAction()
        observeRegisterResult()
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.registerButton.setOnClickListener {
            val name = binding.usernameInput.text.toString().trim()
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            registerViewModel.register(name, email, password)
        }
    }

    private fun observeRegisterResult() {
        registerViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBarRegister.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        registerViewModel.registerResult.observe(this) { result ->
            result.onSuccess { response ->
                if (response.error == false) {
                    showSuccessDialog(binding.emailInput.text.toString())
                } else {
                    Toast.makeText(this, response.message ?: getString(R.string.msg_register_failed), Toast.LENGTH_SHORT).show()
                }
            }.onFailure { error ->
                val errorMessage = if (error is HttpException) {
                    parseErrorMessage(error)
                } else {
                    error.message ?: getString(R.string.msg_error_failed)
                }
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSuccessDialog(toString: String) {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.msg_title_register_success))
            setMessage(getString(R.string.msg_register_success))
            setPositiveButton(getString(R.string.login)) { _, _ ->
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(300)
        val registerDesc = ObjectAnimator.ofFloat(binding.tvRegisterDesc, View.ALPHA, 1f).setDuration(300)
        val usernameField = ObjectAnimator.ofFloat(binding.usernameField, View.ALPHA, 1f).setDuration(300)
        val emailField = ObjectAnimator.ofFloat(binding.emailField, View.ALPHA, 1f).setDuration(300)
        val passwordField = ObjectAnimator.ofFloat(binding.passwordField, View.ALPHA, 1f).setDuration(300)
        val registerButton = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(
                title,
                registerDesc,
                usernameField,
                emailField,
                passwordField,
                registerButton
            )
            startDelay = 100
        }.start()
    }
}
