package com.rifqipadisiliwangi.sismartpju.view.adapter.pekerjaan

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.GoogleMap
import com.rifqipadisiliwangi.sismartpju.R
import com.rifqipadisiliwangi.sismartpju.data.model.pekerjaan.pjuerror.TipePju
import com.rifqipadisiliwangi.sismartpju.databinding.ItemPekerjaanBinding
import com.rifqipadisiliwangi.sismartpju.view.detail.DetailPekerjaanActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdapterPekerjaanItem(
    private val context: Context,
    var listPju : List<TipePju>,
    private val googleMap: GoogleMap
) : RecyclerView.Adapter<AdapterPekerjaanItem.ViewHolder>(){

    class ViewHolder(var binding: ItemPekerjaanBinding):RecyclerView.ViewHolder(binding.root) {
        val view = binding

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPekerjaanItem.ViewHolder {
        val view = ItemPekerjaanBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvIdPekerjaan.text = listPju[position].idpelanggan
        holder.binding.tvIdPju.text = listPju[position].idpju
        holder.binding.tvDate.text =  listPju[position].tgl
//        holder.binding.tvTitleKerusakan.text =  listPju[position].kondisi

        val response = listPju[position].alamat
        val regex = Regex("^Jl\\. ([\\w\\s]+) No\\.(\\d+), ([\\w\\s]+), Kec\\. ([\\w\\s]+), Kabupaten ([\\w\\s]+), Jawa Tengah (\\d{5})$")
        val matchResult = regex.find(response)

        if (matchResult != null) {
            val (jalan, nomor, desa, kecamatan, kabupaten, kodePos) = matchResult.destructured
            holder.binding.tvAddress.text = "Jl. ${"$jalan, $desa, $kecamatan"}"
            println("Jalan: $jalan")
            println("Nomor: $nomor")
            println("Desa: $desa")
            println("Kecamatan-regex: $kecamatan")
            println("Kabupaten: $kabupaten")
            println("Kode Pos: $kodePos")
            println("All-regex: ${jalan + desa + kecamatan}")
        } else {
            holder.binding.tvAddress.text = listPju[position].alamat
        }

        holder.binding.btnDetail.setOnClickListener {
            val intent = Intent(context, DetailPekerjaanActivity::class.java)
            intent.putExtra("idpekerjaan", listPju[position].idpelanggan)
            intent.putExtra("idpju", listPju[position].idpju)
            intent.putExtra("id", listPju[position].idne)
            intent.putExtra("tgl", listPju[position].tgl)
            intent.putExtra("alamat", holder.binding.tvAddress.text.toString())
            intent.putExtra("kondisi", "Kondisi : ${listPju[position].kondisi}")
            intent.putExtra("lat", listPju[position].latitude)
            intent.putExtra("lot", listPju[position].longitude)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return listPju.size
    }
}
