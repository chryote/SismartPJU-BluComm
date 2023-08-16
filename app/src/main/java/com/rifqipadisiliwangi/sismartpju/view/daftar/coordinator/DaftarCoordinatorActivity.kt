package com.rifqipadisiliwangi.sismartpju.view.daftar.coordinator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.rifqipadisiliwangi.sismartpju.R
import com.rifqipadisiliwangi.sismartpju.data.model.daftar.coordinator.CoordinatorSingleton
import com.rifqipadisiliwangi.sismartpju.data.model.pekerjaan.PekerjaanSingleton
import com.rifqipadisiliwangi.sismartpju.databinding.ActivityDaftarCoordinatorBinding
import com.rifqipadisiliwangi.sismartpju.view.adapter.daftar.coordinator.AdapterCoordinatorItem
import com.rifqipadisiliwangi.sismartpju.view.adapter.pekerjaan.AdapterPekerjaanItem

class DaftarCoordinatorActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDaftarCoordinatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarCoordinatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerShown()
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun recyclerShown(){
        binding.rvCoordinator.adapter = AdapterCoordinatorItem(CoordinatorSingleton.listPekerjaan)
        binding.rvCoordinator.layoutManager =  GridLayoutManager(this, 2)
        binding.rvCoordinator.isNestedScrollingEnabled = false
    }
}