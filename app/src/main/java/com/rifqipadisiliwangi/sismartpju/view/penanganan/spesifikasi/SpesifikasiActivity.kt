package com.rifqipadisiliwangi.sismartpju.view.penanganan.spesifikasi

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
import com.rifqipadisiliwangi.sismartpju.databinding.ActivitySpesifikasiBinding

class SpesifikasiActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding : ActivitySpesifikasiBinding
    private var lat : String= ""
    private var lot : String= ""
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpesifikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getBundle()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnPekerjaan.setOnClickListener {
            onBackPressed()
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.tvTitleId.setOnClickListener {
            Toast.makeText(this@SpesifikasiActivity, "ID Pekerjaan : ${binding.tvTitleId.text}", Toast.LENGTH_SHORT).show()
        }


        binding.tvTitlePju.setOnClickListener {
            Toast.makeText(this@SpesifikasiActivity, "ID PJU : ${binding.tvTitleId.text}", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getBundle(){
        binding.tvTitleId.text = intent.extras?.getString("idpekerjaan") ?: "Tidak Terdeteksi"
        binding.tvTitlePju.text = intent.extras?.getString("idpju") ?: "Tidak Terdeteksi"
        binding.tvTitleDate.text = intent.extras?.getString("tgl") ?: "Tidak Terdeteksi"
        binding.tvTitleAddress.text = intent.extras?.getString("alamat") ?: "Tidak Terdeteksi"
        binding.tvKondisi.text = intent.extras?.getString("kondisi") ?: "Tidak Terdeteksi"
        lat = intent.extras?.getString("lat") ?: "Tidak Terdeteksi"
        lot = intent.extras?.getString("lot") ?: "Tidak Terdeteksi"
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        // Menambahkan marker
        if (lat == "" && lot == ""){
            Toast.makeText(this@SpesifikasiActivity, "Lokasi Tidak Tersedia", Toast.LENGTH_SHORT).show()
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

    override fun onBackPressed() {
        super.onBackPressed()
        finishAndRemoveTask()
    }

}