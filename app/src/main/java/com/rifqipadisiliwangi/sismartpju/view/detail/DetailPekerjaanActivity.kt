package com.rifqipadisiliwangi.sismartpju.view.detail

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.rifqipadisiliwangi.sismartpju.R
import com.rifqipadisiliwangi.sismartpju.databinding.ActivityDetailPekerjaanBinding
import com.rifqipadisiliwangi.sismartpju.view.home.DashboardActivity
import com.rifqipadisiliwangi.sismartpju.view.penanganan.pekerjaan.TambahPekerjaanActivity

class DetailPekerjaanActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var binding: ActivityDetailPekerjaanBinding
    private var lat : String= ""
    private var lot : String= ""

    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPekerjaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getBundle()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, TambahPekerjaanActivity::class.java))
        }
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getBundle(){
        binding.tvTitleId.text = intent.extras?.getString("idpekerjaan") ?: "Tidak Terdeteksi"
        binding.tvTitlePju.text = intent.extras?.getString("idpju") ?: "Tidak Terdeteksi"
        binding.tvTitleDate.text = intent.extras?.getString("tgl") ?: "Tidak Terdeteksi"
        binding.tvTitleAddress.text = intent.extras?.getString("alamat") ?: "Tidak Terdeteksi"
        lat = intent.extras?.getString("lat") ?: "Tidak Terdeteksi"
        lot = intent.extras?.getString("lot") ?: "Tidak Terdeteksi"
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