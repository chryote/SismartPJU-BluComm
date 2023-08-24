package com.rifqipadisiliwangi.sismartpju.view.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rifqipadisiliwangi.sismartpju.R
import com.rifqipadisiliwangi.sismartpju.databinding.ActivityDetailPekerjaanBinding
import com.rifqipadisiliwangi.sismartpju.view.penanganan.pekerjaan.TambahPekerjaanActivity

class DetailPekerjaanActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var binding: ActivityDetailPekerjaanBinding
    private var lat : String= ""
    private var lot : String= ""
    private var id : String= ""

    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPekerjaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getBundle()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnAdd.setOnClickListener {
            toSpesifikasi()
        }
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.tvTitleId.setOnClickListener {
            Toast.makeText(this@DetailPekerjaanActivity, "ID Pekerjaan : ${binding.tvTitleId.text}", Toast.LENGTH_SHORT).show()
        }


        binding.tvTitlePju.setOnClickListener {
            Toast.makeText(this@DetailPekerjaanActivity, "ID PJU : ${binding.tvTitleId.text}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getBundle(){
        id = intent.extras?.getString("id") ?: "Tidak Terdeteksi"
        binding.tvTitleId.text = intent.extras?.getString("idpekerjaan") ?: "Tidak Terdeteksi"
        binding.tvTitlePju.text = intent.extras?.getString("idpju") ?: "Tidak Terdeteksi"
        binding.tvTitleDate.text = intent.extras?.getString("tgl") ?: "Tidak Terdeteksi"
        binding.tvTitleAddress.text = intent.extras?.getString("alamat") ?: "Tidak Terdeteksi"
        binding.tvKondisi.text = intent.extras?.getString("kondisi") ?: "Tidak Terdeteksi"
        lat = intent.extras?.getString("lat") ?: "Tidak Terdeteksi"
        lot = intent.extras?.getString("lot") ?: "Tidak Terdeteksi"
    }
    private fun toSpesifikasi(){
        val intent = Intent(this, TambahPekerjaanActivity::class.java)
        intent.putExtra("id",id)
        intent.putExtra("idpekerjaan",binding.tvTitleId.text.toString())
        intent.putExtra("idpju",binding.tvTitleId.text.toString())
        intent.putExtra("tgl", binding.tvTitleDate.text.toString())
        intent.putExtra("alamat", binding.tvTitleAddress.text.toString())
        intent.putExtra("kondisi", binding.tvKondisi.text.toString())
        intent.putExtra("lat", lat)
        intent.putExtra("lot",lot)
        this.startActivity(intent)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        // Menambahkan marker
        if (lat == "" && lot == ""){
            Toast.makeText(this@DetailPekerjaanActivity, "Lokasi Tidak Tersedia", Toast.LENGTH_SHORT).show()
        }else{
            val markerPosition = LatLng(lat.toDouble(), lot.toDouble()) // Contoh koordinat San Francisco
            val marker = MarkerOptions()
                .position(markerPosition)
                .title("Lokasi PJU Error")
            googleMap.addMarker(marker)

            // Memindahkan kamera ke posisi marker
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerPosition, 12f)
            googleMap.animateCamera(cameraUpdate)

        }
    }
}