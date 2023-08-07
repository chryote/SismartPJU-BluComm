package com.rifqipadisiliwangi.sismartpju.view.adapter.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.rifqipadisiliwangi.sismartpju.R
import com.rifqipadisiliwangi.sismartpju.data.utils.BluetoothDeviceMap
import com.rifqipadisiliwangi.sismartpju.databinding.ItemBluetoothBinding
import com.rifqipadisiliwangi.sismartpju.databinding.ItemPekerjaanBinding


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
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.BLUETOOTH_CONNECT
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//
//            return
//        }

        holder.binding.deviceName.setText(bluetoothDevice.name)
        holder.binding.deviceMacadress.setText(bluetoothDevice.address)
        val drawable: Int =
            BluetoothDeviceMap().getDrawable(bluetoothDevice.bluetoothClass.deviceClass)
        holder.binding.deviceIcon.setImageResource(drawable)
        Log.d("hugo", "name: ${bluetoothDevice.name} adres: ${bluetoothDevice.address}")
    }

}