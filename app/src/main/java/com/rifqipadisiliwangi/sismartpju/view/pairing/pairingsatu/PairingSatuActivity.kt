package com.rifqipadisiliwangi.sismartpju.view.pairing.pairingsatu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rifqipadisiliwangi.sismartpju.R
import com.rifqipadisiliwangi.sismartpju.databinding.ActivityPairingSatuBinding
import com.rifqipadisiliwangi.sismartpju.view.home.DashboardActivity

class PairingSatuActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPairingSatuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPairingSatuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, AddpairingActivity::class.java))
        }
    }
}