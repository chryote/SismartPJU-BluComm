package com.rifqipadisiliwangi.sismartpju.view.home.fragment

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.rifqipadisiliwangi.sismartpju.data.model.pekerjaan.pjuerror.TipePju
import com.rifqipadisiliwangi.sismartpju.databinding.FragmentHomeBinding
import com.rifqipadisiliwangi.sismartpju.view.adapter.pekerjaan.AdapterPekerjaanItem
import com.rifqipadisiliwangi.sismartpju.view.map.MapsActivity
import com.rifqipadisiliwangi.sismartpju.viewmodel.ViewModelPekerjaanPju

private const val TAG = "HomeFragment"
class HomeFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapClickListener  {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var pekerjaanAdapter: AdapterPekerjaanItem
    private lateinit var listPju : List<TipePju>
    private lateinit var googleMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private var marker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerShown()

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        binding.etSearchLocation.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                searchLocation()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        locationManager = requireActivity().getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

    }
    private fun recyclerShown(){
        val viewModel = ViewModelProvider(requireActivity()).get(ViewModelPekerjaanPju::class.java)
        viewModel.getPju().observe(viewLifecycleOwner, Observer {
            if (it != null){
                listPju = it.tipe
                binding.rvMonitoring.layoutManager = LinearLayoutManager(requireActivity())
                pekerjaanAdapter = AdapterPekerjaanItem(requireActivity(),listPju, googleMap)
                binding.rvMonitoring.adapter = pekerjaanAdapter
            }else {
                Toast.makeText(requireActivity(),"Data Tidak Tampil", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.callApiPju()
    }

    private fun searchLocation() {
        val locationName = binding.etSearchLocation.text.toString()
        if (locationName == ""){
            Toast.makeText(requireActivity(),"Lokasi Tidak Dapat Tampil", Toast.LENGTH_SHORT).show()
        }else{
            val geocoder = Geocoder(requireActivity())
            val addressList = geocoder.getFromLocationName(locationName, 1)
            if (!addressList.isNullOrEmpty()) {
                val address = addressList[0]
                val latLng = LatLng(address.latitude, address.longitude)

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f))
            } else {
                Toast.makeText(requireActivity(), "Lokasi tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val viewModel = ViewModelProvider(requireActivity()).get(ViewModelPekerjaanPju::class.java)
        viewModel.getPju().observe(viewLifecycleOwner, Observer {
            if (it != null){
                pekerjaanAdapter = AdapterPekerjaanItem(requireActivity(),listPju, googleMap)
                for (irrigationItem in listPju) {
                    val latitude = irrigationItem.latitude.toDoubleOrNull()
                    val longitude = irrigationItem.longitude.toDoubleOrNull()
                    if (latitude != null && longitude != null) {
                        val latLng = LatLng(latitude, longitude)
                        googleMap.addMarker(MarkerOptions().position(latLng).title(irrigationItem.idpju))
                    }
                }
                if (listPju.isNotEmpty()) {
                    val pjuReport = listPju.first()
                    val firstLatLng = LatLng(pjuReport.latitude?.toDoubleOrNull() ?: 0.0,
                        pjuReport.longitude?.toDoubleOrNull() ?: 0.0)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 12f))
                }
            }else {
                Toast.makeText(requireActivity(),"Data Tidak Tampil", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.callApiPju()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Check if GPS is enabled
                if (isGPSEnabled()) {
                    if (ActivityCompat.checkSelfPermission(
                            requireActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            requireActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return
                    }
                    googleMap.isMyLocationEnabled = true
                } else {
                    showGPSDisabledDialog()
                }
            }
        }
    }

    private fun showGPSDisabledDialog() {
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        dialogBuilder.setMessage("GPS is disabled. Enable GPS in the device settings to use this feature.")
            .setCancelable(false)
            .setPositiveButton("Settings") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(settingsIntent)
            }
            .setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }

        val alert = dialogBuilder.create()
        alert.setOnShowListener {
            // Set custom style for the positive button (Yes)
            alert.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
                ContextCompat.getColor(requireActivity(), android.R.color.black))
            // Set custom style for the negative button (No)
            alert.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(
                ContextCompat.getColor(requireActivity(), android.R.color.black))
        }
        alert.show()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
//        val position = marker.position
//        val latitude = position.latitude
//        val longitude = position.longitude
        // Handle the latitude and longitude coordinates as needed
        // For example, display them in a Toast message
//        val message = "Latitude: $latitude, Longitude: $longitude"
//        showToast(message)
        return false
    }

    override fun onMapClick(latLng: LatLng) {
        // Remove the previous marker if exists
        marker?.remove()

        // Add a new marker at the clicked location
        marker = googleMap.addMarker(MarkerOptions().position(latLng).title("My Location"))

        // Move the camera to the clicked location
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        locationManager = requireActivity().getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager
    }


    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    private fun isGPSEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

}