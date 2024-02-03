package com.rifqipadisiliwangi.sismartpju.view.adapter.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import android.graphics.Color
import com.rifqipadisiliwangi.sismartpju.R
import androidx.recyclerview.widget.RecyclerView
import com.rifqipadisiliwangi.sismartpju.databinding.ItemBluetoothBinding
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.io.OutputStream
import java.util.*


import java.io.InputStream
import java.net.Socket
import java.net.SocketTimeoutException

import java.util.UUID
import kotlinx.coroutines.*
import android.bluetooth.BluetoothProfile
import android.os.AsyncTask

interface BluetoothDeviceClickListener {
    fun onBluetoothDeviceSelected(device: BluetoothDevice)
}
var selectedItemPosition = -1
var selectedDevicePosition = ""

class BluetoothDeviceAdapter(
    private val bluetoothDevices: ArrayList<BluetoothDevice>,
    private val context: Context,
    private val deviceAddresses: HashSet<String> = HashSet() // Specify the type explicitly
) : RecyclerView.Adapter<BluetoothDeviceAdapter.ViewHolder>() {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()


    object BluetoothManager {
        var context: Context? = null // Add this line

        fun sendStringToDevice(esp32Address: String, message: String) {
            val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
            val esp32Device: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice(esp32Address)

            if (esp32Device != null) {
                GlobalScope.launch(Dispatchers.IO) {
                    sendData(esp32Device, message)
                }
            } else {
                // Handle the situation where the device with the specified address is not found
            }
        }

        @SuppressLint("MissingPermission")
        private suspend fun sendData(device: BluetoothDevice, message: String) {
            try {
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                val socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
                socket.connect()
                try {
                    val outputStream = socket.outputStream
                    Log.d("outstream", "konek socket")
                    outputStream.write(message.toByteArray())
                    Log.d("outstream", "write message")
                    outputStream.flush()
                    Log.d("outstream", "flush!!")

                    /*try {
                        // Now, set up an InputStream to read the response
                        Log.d("inputStream", "cek respon")
                        val inputStream = socket.inputStream
                        
                        val buffer = ByteArray(1024)
                        val bytesRead = inputStream.read(buffer)


                        // Assuming the response is a String, you can convert it like this
                        val response = String(buffer, 0, bytesRead)

                        // Process the response as needed
                        Log.d("BluetoothManager", "Received response: $response ")

                    } catch (e: Exception){
                        Log.e("blueetooth response input stream", "Error : ${e.message}")
                    }*/

                    outputStream.close()
                    socket.close()

                    // Add log or toast to indicate that the message is sent
                    Log.d("BluetoothManager", "Message sent successfully: $message to ${device.address}")
                } catch (e: Exception) {
                    Log.e("BluetoothTest", "Error: ${e.message}")
                    // Handle the error
                }

                // or
                // Toast.makeText(context, "Message sent successfully to ${device.address}", Toast.LENGTH_SHORT).show()

            } catch (e: IOException) {
                // Handle exceptions here
                e.printStackTrace()

                // Add log or toast to indicate failure to send the message
                Log.e("BluetoothManager", "Failed to send message to ${device.address}")
                // or
                // Toast.makeText(context, "Failed to send message to ${device.address}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun sendStringMessage(socket: BluetoothSocket, message: String) {
        try {
            // Get the output stream from the Bluetooth socket
            val outputStream: OutputStream = socket.outputStream

            // Convert the string to bytes and write to the output stream
            val byteArray: ByteArray = message.toByteArray()
            outputStream.write(byteArray)

        } catch (e: IOException) {
            // Handle exceptions here
            e.printStackTrace()
        }
    }
    @SuppressLint("MissingPermission")
    fun sendStringBluetooth(){
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        val deviceAddress = "24:D7:EB:14:E4:3A"
        val device: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice(deviceAddress)

        val bluetoothGatt: BluetoothGatt? = device?.connectGatt(context, false, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    val characteristicUuid = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb") // Replace with your characteristic UUID
                    val characteristic: BluetoothGattCharacteristic? = gatt.getService(UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb"))?.getCharacteristic(characteristicUuid)

                    val data = "Your string data"
                    characteristic?.value = data.toByteArray()

                    gatt.writeCharacteristic(characteristic)
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    // Disconnected from the device
                }
            }

            override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    // The write operation was successful
                    Log.d("Bluetooth", "Message sent successfully")
                } else {
                    // The write operation failed
                    Log.d("Bluetooth", "Failed to send message")
                }
            }
        }, BluetoothDevice.TRANSPORT_LE)
    }


    @SuppressLint("MissingPermission")
    fun connectToESP32(address: String): BluetoothSocket? {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        val device: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice(address)

        return try {
            val socket: BluetoothSocket? = device?.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
            socket?.connect()
            socket
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun connectAndSendString(address: String, message: String) {
        val socket = connectToESP32(address)

        if (socket != null) {
            try {
                // Get the output stream from the BluetoothSocket
                val outputStream: OutputStream = socket.outputStream

                // Add carriage return and new line to the message
                //val messageWithCRLF = "$message+ "\r\n"
                val messageCRLF = "\r\n"

                // Convert the string to bytes and write it to the output stream
                //val messageBytes = messageWithCRLF.toByteArray(Charsets.UTF_8)
                val messageSend = "610D0A".toByteArray()
                //val messageSendCRLF = messageCRLF.toInt()
                Log.d("Bluetooth Message", "Message sent: $messageSend")
                Log.d("Bluetooth Conn", "Address sent: $address")
                //Log.d("Bluetooth Message CRLF", "Message sent: $messageSendCRLF")
                val inputString = "a\r\n"

                outputStream.write(messageSend)
                //outputStream.write(messageSendCRLF)

                Log.d("BluetoothDeviceConnection", "Message sent: $message")

                // Close the socket when done
                //socket.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            Log.d("BluetoothDeviceConnection", "Connection failed!")
        }
    }


    fun findBluetoothDeviceIndex(deviceAddress: String): Int {
        for ((index, device) in bluetoothDevices.withIndex()) {
            if (device.address == deviceAddress) {
                return index
            }
        }
        // Return -1 if the device with the specified address is not found
        return -1
    }

    fun getBluetoothDevicesList(): ArrayList<BluetoothDevice> {
        return bluetoothDevices
    }

    fun containsDevice(device: BluetoothDevice): Boolean {
        return deviceAddresses.contains(device.address)
    }

    // Add this method to add a new device to the adapter's dataset
    fun addBluetoothDevice(device: BluetoothDevice) {
        bluetoothDevices.add(device)
        notifyDataSetChanged() // Notify the adapter that the dataset has changed
    }

    private var clickListener: BluetoothDeviceClickListener? = null

    fun setClickListener(listener: BluetoothDeviceClickListener) {
        this.clickListener = listener
    }

    fun getSelectedDevicePosition(): String {
        Log.d("getSelectedItemPosition()", "Selected device position: $selectedDevicePosition")
        return selectedDevicePosition
    }

    fun getSelectedBluetoothDevice(number: Int): BluetoothDevice? {
        if (number != -1) {
            Log.d("getSelectedBluetoothDevice()", "Selected bluetooth device: $bluetoothDevices[selectedItemPosition]")
            return bluetoothDevices[number]
        }
        return null
    }


    class ViewHolder(val binding: ItemBluetoothBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = ItemBluetoothBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bluetoothDevices.size
    }

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val bluetoothDevice = bluetoothDevices[position]

        if (selectedItemPosition == position) {
            holder.itemView.setBackgroundColor(context.resources.getColor(R.color.selectedItemColor))
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }

        holder.binding.deviceName.text = bluetoothDevice.name
        holder.binding.deviceMacadress.text = bluetoothDevice.address
        holder.binding.constBluetooth.setOnClickListener {
            clickListener?.onBluetoothDeviceSelected(bluetoothDevice)

            // Extract Bluetooth address from BluetoothDevice
            val bluetoothAddress = bluetoothDevice.address
            Log.d("BluetoothDeviceAdapter", "String address is: $bluetoothAddress")

            Toast.makeText(context, "name: ${bluetoothDevice.name} address: $bluetoothAddress", Toast.LENGTH_SHORT).show()
            Log.d("BluetoothDeviceAdapter", "Selected item position: $selectedItemPosition")

            // Send string to the Bluetooth address
            //BluetoothManager.sendStringToDevice(bluetoothAddress, "haloooo")
            //sendStringBluetooth()
            // Example usage
            /*val address = "24:D7:EB:14:E4:3A"
            val socket = connectToESP32(address)

            if (socket != null) {
                Log.d("BluetoothDeviceConnection", "connected")
            } else {
                Log.d("BluetoothDeviceConnection", "failed!")
            }*/

            val address = "24:D7:EB:14:E4:3A"
            val messageToSend = "halo"

            //connectAndSendString(address, messageToSend)


            selectedDevicePosition = bluetoothDevice.address
            selectedItemPosition = holder.adapterPosition
            notifyDataSetChanged()
        }
        Log.d("hugo", "name: ${bluetoothDevice.name} adres: ${bluetoothDevice.address}")
    }


}


