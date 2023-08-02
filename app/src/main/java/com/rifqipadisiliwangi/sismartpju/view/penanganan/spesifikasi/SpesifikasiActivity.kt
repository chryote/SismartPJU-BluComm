package com.rifqipadisiliwangi.sismartpju.view.penanganan.spesifikasi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rifqipadisiliwangi.sismartpju.databinding.ActivitySpesifikasiBinding
import com.rifqipadisiliwangi.sismartpju.view.penanganan.pekerjaan.TambahPekerjaanActivity

class SpesifikasiActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySpesifikasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpesifikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPekerjaan.setOnClickListener {
            startActivity(Intent(this, TambahPekerjaanActivity::class.java))
            finish()
        }
    }
}