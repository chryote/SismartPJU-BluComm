package com.rifqipadisiliwangi.sismartpju.view.auth

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AlertDialog
import com.rifqipadisiliwangi.sismartpju.R
import com.rifqipadisiliwangi.sismartpju.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private var isPasswordVisible = false
    private var isPasswordVisibleConfirm = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.showPasswordButton.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility()
        }

        binding.showConfirmPasswordButton.setOnClickListener {
            isPasswordVisibleConfirm = !isPasswordVisibleConfirm
            togglePasswordVisibility()
        }

        binding.txtLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finishAndRemoveTask()
        }

    }
    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Show the password
            binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT
            binding.showPasswordButton.setImageResource(R.drawable.ic_register_pass) // Replace with your hide password icon
        } else {
            // Hide the password
            binding.etPassword.inputType =
                InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            binding.showPasswordButton.setImageResource(R.drawable.ic_register_pass) // Replace with your show password icon
        }

        if (isPasswordVisibleConfirm) {
            // Show the password
            binding.etConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT
            binding.showConfirmPasswordButton.setImageResource(R.drawable.ic_register_pass) // Replace with your hide password icon
        } else {
            // Hide the password
            binding.etConfirmPassword.inputType =
                InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            binding.showConfirmPasswordButton.setImageResource(R.drawable.ic_register_pass) // Replace with your show password icon
        }

        // Move the cursor to the end of the password field to maintain cursor position
        binding.etPassword.setSelection(binding.etPassword.text.length)
        binding.etConfirmPassword.setSelection(binding.etConfirmPassword.text.length)
    }

}