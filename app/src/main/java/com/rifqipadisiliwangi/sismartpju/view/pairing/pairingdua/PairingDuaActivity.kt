package com.rifqipadisiliwangi.sismartpju.view.pairing.pairingdua

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rifqipadisiliwangi.sismartpju.databinding.ActivityPairingDuaBinding
import com.rifqipadisiliwangi.sismartpju.view.home.DashboardActivity

class PairingDuaActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPairingDuaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPairingDuaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, AddControllerActivity::class.java))
        }
    }
}