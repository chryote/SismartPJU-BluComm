package com.rifqipadisiliwangi.sismartpju.view.adapter.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rifqipadisiliwangi.sismartpju.databinding.ItemBluetoothBinding
class BluetoothDeviceAdapter(private val bluetoothDevices: ArrayList<BluetoothDevice>, private val context: Context) : RecyclerView.Adapter<BluetoothDeviceAdapter.ViewHolder>() {
    
    class ViewHolder(val binding : ItemBluetoothBinding) : RecyclerView.ViewHolder(binding.root) {

    }

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
        holder.binding.deviceName.setText(bluetoothDevice.name)
        holder.binding.deviceMacadress.setText(bluetoothDevice.address)
        holder.binding.constBluetooth.setOnClickListener {
            Toast.makeText(context,"name: ${bluetoothDevice.name} adres: ${bluetoothDevice.address}", Toast.LENGTH_SHORT ).show()
        }
        Log.d("hugo", "name: ${bluetoothDevice.name} adres: ${bluetoothDevice.address}")
    }

}