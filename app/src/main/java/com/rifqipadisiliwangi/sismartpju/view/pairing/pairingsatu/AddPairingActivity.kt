package com.rifqipadisiliwangi.sismartpju.view.pairing.pairingsatu

import PopupCommandActivity
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.rifqipadisiliwangi.sismartpju.R
import com.rifqipadisiliwangi.sismartpju.databinding.ActivityAddpairingBinding
import com.rifqipadisiliwangi.sismartpju.view.adapter.bluetooth.BluetoothDeviceAdapter
import android.widget.Button
import com.rifqipadisiliwangi.sismartpju.view.home.DashboardActivity

class AddPairingActivity : AppCompatActivity() {

    private val TAG = "bt-dev"
    private val REQUEST_ENABLE_BT = 1

    var bluetoothAdapter: BluetoothAdapter? = null
    var newDevicesRecycler: RecyclerView? = null
    var progressIndicator: LinearProgressIndicator? = null
    var scanButton: ImageView? = null
    var enableBluetooth: MaterialButton? = null
    var buttonCommand: MaterialButton? = null

    var pairedDevices = ArrayList<BluetoothDevice>()
    var bluetoothDeviceAdapter: BluetoothDeviceAdapter = BluetoothDeviceAdapter(pairedDevices, this)

    var newDevices = ArrayList<BluetoothDevice>()
    var newDevicesAdapter: BluetoothDeviceAdapter = BluetoothDeviceAdapter(newDevices, this)


    private val newDevicesReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            // Get the bluetoothDevices list
            val devicesList = bluetoothDeviceAdapter.getBluetoothDevicesList()
            Log.d(TAG, "111Current devices: $devicesList)")
            Log.d(TAG, "Bluetooth Adapter State: ${bluetoothAdapter!!.state}")
            if (BluetoothDevice.ACTION_FOUND == action) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                checkPermission()
                if (device!!.name == null) {
                    return
                }
                if (newDevices.contains(device)) {
                    return
                }

                //                String deviceName = device.getName();
//                String deviceHardwareAddress = device.getAddress(); // MAC address
//                Log.i("bt-dev", deviceName + " " + deviceHardwareAddress);
                if (!bluetoothDeviceAdapter.containsDevice(device)) {
                    bluetoothDeviceAdapter.addBluetoothDevice(device) // Add the device to the adapter
                    // Log the newly discovered device
                    Log.d(TAG, "New Bluetooth Device Added: ${device.name} (${device.address})")
                }

                newDevices.add(device)
                Log.i("array list Bluetooth Devices", "$newDevices")
                newDevicesAdapter.notifyDataSetChanged()
                // Log the newly discovered device
                Log.d(TAG, "New Bluetooth Device Found: ${device.name} (${device.address})")
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED == action) {
                progressIndicator!!.show()
                newDevices.clear()
                Log.i("bt-dev", "discovery started")
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                progressIndicator!!.hide()
                Log.i("bt-dev", "discovery finished")
            }
        }
    }

    var discoverableStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == BluetoothAdapter.ACTION_SCAN_MODE_CHANGED) {
                val mode =
                    intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR)
                when (mode) {
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE -> {
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability enabled.")
                    }

                    BluetoothAdapter.SCAN_MODE_CONNECTABLE -> {
                        Log.d(
                            TAG,
                            "mBroadcastReceiver2: Discoverability disabled. Able to receive connections."
                        )
                    }

                    BluetoothAdapter.SCAN_MODE_NONE -> {
                        Log.d(
                            TAG,
                            "mBroadcastReceiver2: Discoverability disabled. Not able to receive connections."
                        )
                    }
                }
            }
        }
    }
    var bluetoothStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state =
                    intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)
                when (state) {
                    BluetoothAdapter.STATE_OFF -> {
                        enableBluetooth!!.visibility = View.VISIBLE
                    }

                    BluetoothAdapter.STATE_ON -> {
                        Log.d(TAG, "Bluetooth Adapter State!: ${bluetoothAdapter!!.state}")
                        enableBluetooth!!.visibility = View.GONE
                        checkPermission()
                        //                        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        pairedDevices = java.util.ArrayList(bluetoothAdapter!!.bondedDevices)
                        bluetoothDeviceAdapter = BluetoothDeviceAdapter(
                            pairedDevices,
                            applicationContext
                        )
                        Log.d(TAG, "onReceive: ")
                        bluetoothDeviceAdapter.notifyDataSetChanged()
                        if (!bluetoothAdapter!!.isDiscovering) {
                            scanButton!!.performClick()
                        }
                    }
                }
            }
        }
    }

    private lateinit var binding : ActivityAddpairingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddpairingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }


        checkPermission()


        newDevicesRecycler = findViewById(R.id.newDevicesRecyclerView)

        progressIndicator = findViewById(R.id.progressIndicator)
        scanButton = findViewById(R.id.scanDevice)
        enableBluetooth = findViewById(R.id.bluetoothEnables)
        buttonCommand = findViewById(R.id.buttonCommand)
        buttonCommand?.setOnClickListener {
            val popup = PopupCommandActivity(this) {
                // Handle connection without passing any parameters initially
                // This is where you can initiate the connection using the selected Bluetooth device and user input
                // No need to use connectionParameters in this case
            }
            popup.show()
        }

        progressIndicator!!.hide()

        fun pairBluetoothDevice(device: BluetoothDevice) {
            Log.d(TAG, "Current Device List: $device")
            Log.d(TAG, "(Pair Button)Selected Bluetooth Device for Pairing: ${device.name} (${device.address})")
            try {
                val isPaired = device.createBond()
                if (isPaired) {
                    // Pairing initiated successfully
                    Log.d(TAG, "Pairing initiated with ${device.name} (${device.address})")
                } else {
                    // Pairing failed
                    Log.d(TAG, "Pairing failed with ${device.name} (${device.address})")
                }
            } catch (e: Exception) {
                // Handle any exceptions
                Log.e(TAG, "Error pairing with device: ${e.message}")
            }
        }

        val pairButton = findViewById<Button>(R.id.pairButton)
        pairButton.setOnClickListener {
            // Handle the "Pair" button click here
            val selectedDevice = bluetoothDeviceAdapter.getSelectedDevicePosition()
            val selectedPosition = bluetoothDeviceAdapter.findBluetoothDeviceIndex(selectedDevice)
            Log.d(TAG, "Current Position: $selectedPosition)")
            if (selectedPosition != -1) {

                val selectedDevice = bluetoothDeviceAdapter.getSelectedBluetoothDevice(selectedPosition)
                if (selectedDevice != null) {
                    pairBluetoothDevice(selectedDevice)
                }
            } else {
                // Handle the case where no device is selected
                // You can show a message to the user
                Toast.makeText(this, "No device selected for pairing", Toast.LENGTH_SHORT).show()
            }
        }


//      Check for bluetooth availability
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            Toast.makeText(this, "Bluetooth nie jest wspierany", Toast.LENGTH_SHORT).show()
        }

        val bluetoothManager = getSystemService(
            BluetoothManager::class.java
        )
        bluetoothAdapter = bluetoothManager.adapter

        if (!bluetoothAdapter!!.isEnabled) {
//            availibilityIcon.setBackgroundColor(Color.RED);
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        } else {
//            availibilityIcon.setBackgroundColor(Color.BLUE);
        }

        enableBluetooth!!.setOnClickListener({ l: View? ->
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        })

        pairedDevices = ArrayList(bluetoothAdapter!!.bondedDevices)
        bluetoothDeviceAdapter = BluetoothDeviceAdapter(pairedDevices, this)

        newDevicesRecycler!!.adapter = newDevicesAdapter



        val newDevicesFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        newDevicesFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        newDevicesFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(newDevicesReceiver, newDevicesFilter)

        val discoverableStateFilter = IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)

        registerReceiver(discoverableStateReceiver, discoverableStateFilter)

        val bluetoothStateFilter = IntentFilter()
        bluetoothStateFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(bluetoothStateReceiver, bluetoothStateFilter)

        scanButton!!.setOnClickListener({ l: View? ->
            checkBluetoothPermission()
            if (bluetoothAdapter!!.isDiscovering) {
                Log.v("bt-dev", "canceling discovering")
                bluetoothAdapter!!.cancelDiscovery()
            } else {
                Log.d(TAG, "Bluetooth Adapter State: ${bluetoothAdapter!!.state}")
                Log.v("bt-dev", "start discovering")
                bluetoothAdapter!!.cancelDiscovery()
                bluetoothAdapter!!.startDiscovery()
            }
        })


        if (!bluetoothAdapter!!.isDiscovering) {
            scanButton!!.performClick()
        }

    }

    fun checkPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 3
            )
        }
    }

    fun checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                    Manifest.permission.BLUETOOTH_SCAN
                ) == PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN
                    ), 2
                )
            }
        } else {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                    Manifest.permission.BLUETOOTH_ADMIN
                ) == PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN
                    ), 2
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        checkPermission()
        bluetoothAdapter!!.cancelDiscovery()
        unregisterReceiver(newDevicesReceiver)
        unregisterReceiver(discoverableStateReceiver)
        unregisterReceiver(bluetoothStateReceiver)
    }
}