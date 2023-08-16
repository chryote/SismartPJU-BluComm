package com.rifqipadisiliwangi.sismartpju.view.adapter.pekerjaan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rifqipadisiliwangi.sismartpju.R
import com.rifqipadisiliwangi.sismartpju.data.model.pekerjaan.pjuerror.TipePju
import com.rifqipadisiliwangi.sismartpju.databinding.ItemPekerjaanBinding

class AdapterPekerjaanItem(var listPju : List<TipePju>) : RecyclerView.Adapter<AdapterPekerjaanItem.ViewHolder>(){

    class ViewHolder(var binding: ItemPekerjaanBinding):RecyclerView.ViewHolder(binding.root) {
        val view = binding

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPekerjaanItem.ViewHolder {
        val view = ItemPekerjaanBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterPekerjaanItem.ViewHolder, position: Int) {
//        Glide.with(holder.itemView.context).load("https://image.tmdb.org/t/p/w185"+listMovie[position].posterPath).into(holder.binding.imgPoster)
        holder.binding.tvIdPekerjaan.text = listPju[position].idpju
        holder.binding.tvAddress.text = listPju[position].alamat

    }

    override fun getItemCount(): Int {
        return listPju.size
    }
}
