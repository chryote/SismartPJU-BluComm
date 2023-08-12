package com.rifqipadisiliwangi.sismartpju.view.auth

import android.R.attr.duration
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
import com.rifqipadisiliwangi.sismartpju.R
import com.rifqipadisiliwangi.sismartpju.databinding.ActivityLoginBinding
import com.rifqipadisiliwangi.sismartpju.view.pairing.pairingdua.AddControllerActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private var isPasswordVisible = false
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val mediaType = "text/plain".toMediaType()
            val body = "{\r\n\"username\": \"${binding.etUsername.text.toString().trimIndent()}\",\r\n\"password\": \"${binding.etPassword.text.toString().trimIndent()}\" \r\n}".toRequestBody(mediaType)
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://sisemarpju.smartlinks.id/dd163577ea063b814f85b490a748d583?")
                .post(body)
                .addHeader("Content-Type", "text/plain")
                .addHeader("Authorization", "Basic RGlzaHVidXNlcjIxMjp1c2VyRGlzaHViMjEy")
                .build()

            // val responses: Response = client.newCall(requests).execute()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val loginResponse = e.toString()
                    loginResponse.let {
                        Toast.makeText(this@LoginActivity,
                            "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body
                        loginResponse?.let {
                            response.body?.string()?.let { it1 -> Log.d("okhttp-response", it1) }
                            startActivity(Intent(this@LoginActivity, AddControllerActivity::class.java))
                        }
                        runOnUiThread {
                            Toast.makeText(this@LoginActivity,
                                "Login Sukses", Toast.LENGTH_SHORT).show()
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

        binding.showPassBtn.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility()
        }

        binding.etPassword.addTextChangedListener(object  : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (binding.etPassword.text.toString().length < 8){
                    binding.btnLogin.isGone = true
                    binding.btnLoginInvisible.isGone = false
                } else{
                    binding.btnLogin.isGone = false
                    binding.btnLoginInvisible.isGone = true
                }
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
    fun getResponDesc(raw: String): String {
        val obj = JSONObject(raw)
        return obj.getString("Sukses")
    }

}
