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
import com.dicoding.dicodingstoryapp.databinding.ActivityLoginBinding
import com.dicoding.dicodingstoryapp.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.passwordInput.setErrorListener { hasError ->
            binding.buttonLogin.isEnabled = !hasError
        }

        setupAction()
        playAnimation()
        observeLoginResult()
    }


    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            viewModel.login(email, password)
        }
    }

    private fun observeLoginResult() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.buttonLogin.isEnabled = !isLoading
            binding.progressBarLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.loginResult.observe(this) { result ->
            result.onSuccess { response ->
                if (response.error == false && response.loginResult != null) {
                    showSuccessDialog(response.loginResult.name ?: getString(R.string.user))
                } else {
                    Toast.makeText(this, response.message ?: getString(R.string.msg_login_failed), Toast.LENGTH_SHORT).show()
                }
            }.onFailure { error ->
                Toast.makeText(this, getString(R.string.error, error.message), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSuccessDialog(name: String) {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.msg_title_login_success))
            setMessage(getString(R.string.msg_login_success, name))
            setPositiveButton(getString(R.string.next)) { _, _ ->
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun playAnimation() {
        val tvLogin = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.emailField, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.passwordField, View.ALPHA, 1f).setDuration(500)
        val button = ObjectAnimator.ofFloat(binding.buttonLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                tvLogin,
                email,
                password,
                button
            )
            startDelay = 100
        }.start()
    }
}
