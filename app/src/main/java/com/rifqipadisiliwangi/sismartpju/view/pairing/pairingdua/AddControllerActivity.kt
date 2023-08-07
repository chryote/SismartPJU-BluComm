package com.rifqipadisiliwangi.sismartpju.view.pairing.pairingdua

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rifqipadisiliwangi.sismartpju.databinding.ActivityAddConrollerBinding
import com.rifqipadisiliwangi.sismartpju.view.home.DashboardActivity

class AddControllerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddConrollerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddConrollerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }


    }
}