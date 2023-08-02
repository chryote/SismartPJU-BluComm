package com.rifqipadisiliwangi.sismartpju.view.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.rifqipadisiliwangi.sismartpju.R
import com.rifqipadisiliwangi.sismartpju.data.model.pekerjaan.PekerjaanSingleton
import com.rifqipadisiliwangi.sismartpju.databinding.FragmentHomeBinding
import com.rifqipadisiliwangi.sismartpju.view.adapter.pekerjaan.AdapterPekerjaanItem

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerShown()
    }

    private fun recyclerShown(){
        binding.rvMonitoring.adapter = AdapterPekerjaanItem(PekerjaanSingleton.listPekerjaan)
        binding.rvMonitoring.layoutManager =  LinearLayoutManager(requireActivity())
        binding.rvMonitoring.isNestedScrollingEnabled = false
    }

}