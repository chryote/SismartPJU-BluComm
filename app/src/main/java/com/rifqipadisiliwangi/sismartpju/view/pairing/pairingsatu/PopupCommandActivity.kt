import android.annotation.SuppressLint
import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.rifqipadisiliwangi.sismartpju.R
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

class PopupCommandActivity(context: Context, private val callback: (String) -> Unit) : Dialog(context) {



    private lateinit var spinnerDevice: Spinner
    private lateinit var editTextInput: EditText
    private lateinit var buttonConnect: Button
    data class BluetoothDeviceData(val name: String, val address: String)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.command_popup_layout)

        spinnerDevice = findViewById(R.id.spinnerDevice)
        editTextInput = findViewById(R.id.editTextInput)
        buttonConnect = findViewById(R.id.buttonConnect)

        // Populate spinner with paired Bluetooth devices
        val pairedDeviceNames = getPairedBluetoothDevices().map { it.name }
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, pairedDeviceNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDevice.adapter = adapter


        buttonConnect.setOnClickListener {
            // Retrieve the selected item (name) from the spinner
            val selectedDeviceName = spinnerDevice.selectedItem as String

            // Find the corresponding BluetoothDeviceData object
            val selectedDevice = getPairedBluetoothDevices().find { it.name == selectedDeviceName }

            // Use selectedDevice.address as the selected option
            val selectedOption = selectedDevice?.address ?: ""
            val userInput = editTextInput.text.toString()
            Log.e("selected address", "Failed to send message to $selectedOption")
            try {
                connectAndSendString(selectedOption, userInput)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            // Use selectedOption and userInput as parameters for connection
            // callback.invoke("$selectedOption - $userInput")
            // dismiss()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getPairedBluetoothDevices(): List<BluetoothDeviceData> {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        val pairedDevices: Set<android.bluetooth.BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        val deviceList: MutableList<BluetoothDeviceData> = mutableListOf()

        pairedDevices?.forEach { device ->
            val deviceData = BluetoothDeviceData(device.name, device.address)
            deviceList.add(deviceData)
        }

        return deviceList
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
        var socket: BluetoothSocket? = null

        try {
            // Connect to the Bluetooth device
            socket = connectToESP32(address)

            if (socket != null) {
                // Get the output stream from the BluetoothSocket
                val outputStream: OutputStream = socket.outputStream
                try {


                    // Add carriage return and new line to the message
                    val messageCRLF = "\r\n"
                    val messageWithCRLF = "$message$messageCRLF"

                    // Convert the string to bytes and write it to the output stream
                    val messageBytes = messageWithCRLF.toByteArray(Charsets.UTF_8)
                    outputStream.write(messageBytes)

                    Log.d("BluetoothDeviceConnection", "Message sent: $messageWithCRLF")
                    // Add a delay before closing the socket (e.g., sleep for 2 seconds)
                    Thread.sleep(3000)

                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    // Close the output stream
                    outputStream.close()
                }
            } else {
                Log.d("BluetoothDeviceConnection", "Connection failed!")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            // Close the socket when done
            try {
                socket?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}