package com.rifqipadisiliwangi.sismartpju.view.adapter.pekerjaan

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rifqipadisiliwangi.sismartpju.data.model.pekerjaan.Pekerjaan
import com.rifqipadisiliwangi.sismartpju.databinding.ItemPekerjaanBinding
import com.rifqipadisiliwangi.sismartpju.view.detail.DetailPekerjaanActivity

class AdapterPekerjaanItem(private var pekerjaan : ArrayList<Pekerjaan>): RecyclerView.Adapter<AdapterPekerjaanItem.ListViewHolder>() {

    class ListViewHolder(val binding : ItemPekerjaanBinding): RecyclerView.ViewHolder(binding.root){

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        var view = ItemPekerjaanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.tvIdPekerjaan.text = pekerjaan[position].idPju
        holder.binding.tvAddress.text = pekerjaan[position].address
        holder.binding.tvIdPju.text = pekerjaan[position].idPju
//        holder.binding.tvAlert.text = pekerjaan[position].alert
//        holder.binding.tvStatus.text = pekerjaan[position].status

        holder.binding.btnDetail.setOnClickListener {
            val context = it.context
            context.startActivity(Intent(it.context, DetailPekerjaanActivity::class.java))
        }
    }

    fun setDataPekerjaan(PekerjaanList : ArrayList<Pekerjaan>){
        this.pekerjaan = PekerjaanList
    }


    override fun getItemCount(): Int {
        return pekerjaan.size
    }
}