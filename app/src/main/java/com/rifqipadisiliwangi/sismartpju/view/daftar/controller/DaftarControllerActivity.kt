package com.rifqipadisiliwangi.sismartpju.view.daftar.controller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.rifqipadisiliwangi.sismartpju.data.model.daftar.coordinator.CoordinatorSingleton
import com.rifqipadisiliwangi.sismartpju.databinding.ActivityDaftarControllerBinding
import com.rifqipadisiliwangi.sismartpju.view.adapter.daftar.coordinator.AdapterCoordinatorItem

class DaftarControllerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDaftarControllerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarControllerBinding.inflate(layoutInflater)
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