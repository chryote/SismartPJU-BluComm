package com.rifqipadisiliwangi.sismartpju.view.auth

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.rifqipadisiliwangi.sismartpju.R
import com.rifqipadisiliwangi.sismartpju.data.model.login.LoginRequestIem
import com.rifqipadisiliwangi.sismartpju.data.network.ApiClient
import com.rifqipadisiliwangi.sismartpju.databinding.ActivityLoginBinding
import com.rifqipadisiliwangi.sismartpju.view.home.DashboardActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val TAG = "LoginActivity"
class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var sharedpreferences: SharedPreferences


    private var username: String? = null
    private var password: String? = null

    private var isPasswordVisible = false
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginRequest()
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        binding.showPassBtn.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility()
        }

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etPassword.text.toString().length <= 6) {
                    binding.btnLogin.isGone = true
                    binding.btnLoginInvisible.isGone = false
                } else {
                    binding.btnLogin.isGone = false
                    binding.btnLoginInvisible.isGone = true
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Show the password
            binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT
            binding.showPassBtn.setImageResource(R.drawable.ic_register_pass) // Replace with your hide password icon
        } else {
            // Hide the password
            binding.etPassword.inputType =
                InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            binding.showPassBtn.setImageResource(R.drawable.ic_register_pass) // Replace with your show password icon
        }

        // Move the cursor to the end of the password field to maintain cursor position
        binding.etPassword.setSelection(binding.etPassword.text.length)
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun loginRequest(){

        binding.btnLogin.setOnClickListener {


            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Logging in...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            GlobalScope.launch(Dispatchers.Main) {
                username = binding.etUsername.text.toString()
                password = binding.etPassword.text.toString()
                // Show progress dialog

                val response = ApiClient.instance.login(LoginRequestIem(username!!, password!!))
                if (response.isSuccessful) {
                    progressDialog.dismiss() // Dismiss progress dialog
                    runOnUiThread {
                        val loginResponse = response.body()?.tipe
                        if (loginResponse == null){
                            Toast.makeText(this@LoginActivity, "Username atau password salah", Toast.LENGTH_SHORT).show()
                        }else{
                            val editor = sharedpreferences.edit()
                            editor.putString(USERNAME_KEY, username)
                            editor.putString(PASSWORD_KEY, password)
                            editor.apply()
                            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                            Toast.makeText(this@LoginActivity, "Berhasil Login", Toast.LENGTH_SHORT).show()
                            finish()
                            Log.d(TAG, loginResponse.toString())
                        }
                    }
                } else {
                    // Login gagal
                    Log.d(TAG, "Response: ${response.errorBody()}")
                }
            }
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        AlertDialog.Builder(this)
            .setTitle("Tutup Aplikasi")
            .setMessage("Ingin Keluar Aplikasi?")
            .setPositiveButton("Ya"){ _: DialogInterface, i: Int ->
                finishAffinity()
            }
            .setNegativeButton("Tidak"){ dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
            .show()
    }
    override fun onStart() {
        super.onStart()
        if (username != null && password != null) {
            val i = Intent(this@LoginActivity, DashboardActivity::class.java)
            startActivity(i)
        }
    }
    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val USERNAME_KEY = "username_key"
        const val PASSWORD_KEY = "password_key"
    }
}
