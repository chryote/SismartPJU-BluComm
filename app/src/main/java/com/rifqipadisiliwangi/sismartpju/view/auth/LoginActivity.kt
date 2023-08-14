package com.rifqipadisiliwangi.sismartpju.view.auth

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
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
import com.google.gson.Gson
import com.rifqipadisiliwangi.sismartpju.R
import com.rifqipadisiliwangi.sismartpju.data.model.login.LoginResponse
import com.rifqipadisiliwangi.sismartpju.data.model.login.LoginSuccessResponse
import com.rifqipadisiliwangi.sismartpju.databinding.ActivityLoginBinding
import com.rifqipadisiliwangi.sismartpju.view.home.DashboardActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

private val TAG = "LoginActivity"
class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private var isPasswordVisible = false
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.showPassBtn.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility()
        }


        binding.btnLogin.setOnClickListener {
            doLogin()
        }
        binding.etPassword.addTextChangedListener(object  : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etPassword.text.toString().length <= 6 ){
                    binding.btnLogin.isGone = true
                    binding.btnLoginInvisible.isGone = false
                } else{
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

    private fun doLogin(){
        binding.btnLogin.setOnClickListener {
            val mediaType = "text/plain".toMediaType()
            val body = ("{\r\n\"username\": \"${binding.etUsername.text.toString().trimIndent()}\",\r\n\"password\": " +
                    "\"${binding.etPassword.text.toString().trimIndent()}\" \r\n}").toRequestBody(mediaType)
            val request = Request.Builder()
                .url("https://sisemarpju.smartlinks.id/dd163577ea063b814f85b490a748d583?")
                .post(body)
                .addHeader("Content-Type", "text/plain")
                .addHeader("Authorization", "Basic RGlzaHVidXNlcjIxMjp1c2VyRGlzaHViMjEy")
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val loginResponse = e.toString()
                    loginResponse.let {
                        Toast.makeText(this@LoginActivity,
                            "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        runOnUiThread {
                            val loginResponse = response.body
                            loginResponse?.let {
                                var parseObj = ""
                                parseObj =
                                    response.body?.string()?.toString().toString()
                                Log.d("okhttp-response", parseObj)
//                                parseObj = parsingBody
                                val stringObj = """
                                    {"tipe":[{"statusonline":"Offline","statususer":"kedaireka"}],"Respon_code":"1010","Respon_desc":"Sukses"}
                                """.trimIndent()
                                if (stringObj == parseObj){
                                    startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                                    Toast.makeText(this@LoginActivity, "Berhasil Login", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else{
                                Toast.makeText(this@LoginActivity, "Username atau Password salah", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        // Handle API error here
                        // You can extract the error message from the response
                        // using response.errorBody()?.string()
                        runOnUiThread {
                            Toast.makeText(this@LoginActivity,
                                "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
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
