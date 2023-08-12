package com.rifqipadisiliwangi.sismartpju.view.home.fragment

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.rifqipadisiliwangi.sismartpju.data.model.pekerjaan.PekerjaanSingleton
import com.rifqipadisiliwangi.sismartpju.databinding.FragmentHomeBinding
import com.rifqipadisiliwangi.sismartpju.view.adapter.pekerjaan.AdapterPekerjaanItem
import com.rifqipadisiliwangi.sismartpju.view.map.MapsActivity

class HomeFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapClickListener  {

    private lateinit var binding : FragmentHomeBinding

    private lateinit var googleMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private var marker: Marker? = null
    private lateinit var sharedPreferences: SharedPreferences

    private var latitude: String? = null
    private var longitude: String? = null

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

        binding.cvMaps.setOnClickListener {
            startActivity(Intent(context, MapsActivity::class.java))
        }

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)


        locationManager = requireActivity().getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Set marker click listener
        googleMap.setOnMarkerClickListener(this)

        // Set map click listener
        googleMap.setOnMapClickListener(this)

        // Check for location permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            // Check if GPS is enabled
            if (isGPSEnabled()) {
                googleMap.isMyLocationEnabled = true

                // Check if latitude and longitude are available
                if (latitude != null && longitude != null) {
                    // Create LatLng object with the received latitude and longitude
                    val location = latitude!!.toDoubleOrNull()?.let { longitude!!.toDoubleOrNull()
                        ?.let { it1 -> LatLng(it, it1) } }

                    // Add a marker at the received location
                    marker = location?.let { MarkerOptions().position(it).title("My Location") }
                        ?.let { googleMap.addMarker(it) }

                    // Move the camera to the received location
                    location?.let { CameraUpdateFactory.newLatLngZoom(it, 12f) }
                        ?.let { googleMap.moveCamera(it) }
                } else {
                    // Latitude and longitude not available, handle the default behavior
                    // ...

                    // For example, move the camera
                    // Get the user's current location
                    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
                    fusedLocationProviderClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            if (location != null) {
                                // If location is available, move the camera to the current location
                                val currentLocation = LatLng(location.latitude, location.longitude)
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12f))
                            } else {
                                // If location is not available, move the camera to Indonesia
                                val indonesiaLocation = LatLng(-0.7893, 113.9213)
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indonesiaLocation, 5f))
                            }
                        }
                }
            } else {
                // If GPS is not enabled, move the camera to Indonesia
                val indonesiaLocation = LatLng(-0.7893, 113.9213)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indonesiaLocation, 5f))
                showGPSDisabledDialog()
            }
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
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


    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private fun recyclerShown(){
        binding.rvMonitoring.adapter = AdapterPekerjaanItem(PekerjaanSingleton.listPekerjaan)
        binding.rvMonitoring.layoutManager =  LinearLayoutManager(requireActivity())
        binding.rvMonitoring.isNestedScrollingEnabled = false
    }

}