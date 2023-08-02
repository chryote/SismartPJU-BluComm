package com.rifqipadisiliwangi.sismartpju.view.pairing.pairingsatu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rifqipadisiliwangi.sismartpju.R
import com.rifqipadisiliwangi.sismartpju.databinding.ActivityAddpairingBinding
import com.rifqipadisiliwangi.sismartpju.view.home.DashboardActivity

class AddpairingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddpairingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddpairingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, PairingSatuActivity::class.java))
            finish()
        }
        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
    }
}