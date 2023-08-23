package com.rifqipadisiliwangi.sismartpju.view.auth

import android.annotation.SuppressLint
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
import androidx.lifecycle.MutableLiveData
import com.rifqipadisiliwangi.sismartpju.R
import com.rifqipadisiliwangi.sismartpju.data.model.login.LoginRequestIem
import com.rifqipadisiliwangi.sismartpju.data.model.login.LoginResponseItem
import com.rifqipadisiliwangi.sismartpju.data.network.ApiClient
import com.rifqipadisiliwangi.sismartpju.data.network.ApiService
import com.rifqipadisiliwangi.sismartpju.databinding.ActivityLoginBinding
import com.rifqipadisiliwangi.sismartpju.view.home.DashboardActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

private const val TAG = "LoginActivity"
class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var sharedPrefs: SharedPreferences

    private var isPasswordVisible = false
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginRequest()

//        sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//        val isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false)
//        if (isLoggedIn) {
//            startActivity(Intent(this, DashboardActivity::class.java))
//        } else {
//            // Handle login button click
//            binding.btnLogin.setOnClickListener {
//                doLogin()
//                // Perform login validation
//                if (binding.etUsername.text.toString()
//                        .isNotEmpty() && binding.etPassword.text.toString().isNotEmpty()
//                ) {
//                    // Save logged in status to SharedPreferences
//                    val editor = sharedPrefs.edit()
//                    editor.putBoolean("isLoggedIn", true)
//                    editor.apply()
//                    startActivity(Intent(this, DashboardActivity::class.java))
//                } else {
//                    Toast.makeText(this, "Please enter valid credentials", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }

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
    private fun loginRequest(){

        binding.btnLogin.setOnClickListener {

            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Logging in...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            GlobalScope.launch(Dispatchers.Main) {
                val username = binding.etUsername.text.toString()
                val password = binding.etPassword.text.toString()
                // Show progress dialog

                val response = ApiClient.instance.login(LoginRequestIem(username, password))
                if (response.isSuccessful) {
                    progressDialog.dismiss() // Dismiss progress dialog
                    runOnUiThread {
                        val loginResponse = response.body()
                        loginResponse?.let {
                            val parseObj = response.body().toString()
                            Log.d("okhttp-response", parseObj)
//                                parseObj = parsingBody
                            val stringObj ="LoginResponseItem(Respon_code=1010, Respon_desc=Sukses, tipe=[Tipe(statusonline=Offline, statususer=kedaireka)])"
                            if (stringObj == parseObj){
                                startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                                Toast.makeText(this@LoginActivity, "Berhasil Login", Toast.LENGTH_SHORT).show()
                                finish()
                            } else{
                                Toast.makeText(this@LoginActivity, "Username atau Password salah", Toast.LENGTH_SHORT).show()
                            }
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
}
