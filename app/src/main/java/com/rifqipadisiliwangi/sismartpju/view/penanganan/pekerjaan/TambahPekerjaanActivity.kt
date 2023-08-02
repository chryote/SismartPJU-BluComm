package com.rifqipadisiliwangi.sismartpju.view.penanganan.pekerjaan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rifqipadisiliwangi.sismartpju.R
import com.rifqipadisiliwangi.sismartpju.databinding.ActivityTambahPekerjaanBinding
import com.rifqipadisiliwangi.sismartpju.view.penanganan.spesifikasi.SpesifikasiActivity

class TambahPekerjaanActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTambahPekerjaanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahPekerjaanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSpesifikasi.setOnClickListener {
            startActivity(Intent(this, SpesifikasiActivity::class.java))
            finish()
        }
    }
}