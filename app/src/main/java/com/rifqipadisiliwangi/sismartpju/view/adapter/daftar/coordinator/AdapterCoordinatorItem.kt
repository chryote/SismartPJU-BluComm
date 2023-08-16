package com.rifqipadisiliwangi.sismartpju.view.adapter.daftar.coordinator

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rifqipadisiliwangi.sismartpju.data.model.daftar.coordinator.Coordinator
import com.rifqipadisiliwangi.sismartpju.databinding.ItemDaftarCoordinatorBinding
import com.rifqipadisiliwangi.sismartpju.databinding.ItemPekerjaanBinding
import com.rifqipadisiliwangi.sismartpju.view.detail.DetailPekerjaanActivity

class AdapterCoordinatorItem(private var pekerjaan : ArrayList<Coordinator>): RecyclerView.Adapter<AdapterCoordinatorItem.ListViewHolder>() {

    class ListViewHolder(val binding : ItemDaftarCoordinatorBinding): RecyclerView.ViewHolder(binding.root){

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        var view = ItemDaftarCoordinatorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.tvIdPekerjaan.text = pekerjaan[position].idPju
        holder.binding.tvAddress.text = pekerjaan[position].address
//        holder.binding.tvAlert.text = pekerjaan[position].alert
//        holder.binding.tvStatus.text = pekerjaan[position].status

//        holder.binding.btnDetail.setOnClickListener {
//            val context = it.context
//            context.startActivity(Intent(it.context, DetailPekerjaanActivity::class.java))
//        }
    }



    fun setDataPekerjaan(PekerjaanList : ArrayList<Coordinator>){
        this.pekerjaan = PekerjaanList
    }


    override fun getItemCount(): Int {
        return pekerjaan.size
    }
}