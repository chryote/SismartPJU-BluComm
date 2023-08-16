package com.rifqipadisiliwangi.sismartpju.view.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.rifqipadisiliwangi.sismartpju.R
import com.rifqipadisiliwangi.sismartpju.data.model.pekerjaan.PekerjaanSingleton
import com.rifqipadisiliwangi.sismartpju.databinding.FragmentPekerjaanBinding
import com.rifqipadisiliwangi.sismartpju.view.adapter.pekerjaan.AdapterPekerjaanItem

class PekerjaanFragment : Fragment() {

    private lateinit var binding : FragmentPekerjaanBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPekerjaanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        recyclerShown()

    }

//    private fun recyclerShown(){
//        binding.rvPekerjaan.adapter = AdapterPekerjaanItem(PekerjaanSingleton.listPekerjaan)
//        binding.rvPekerjaan.layoutManager =  LinearLayoutManager(requireActivity())
//        binding.rvPekerjaan.isNestedScrollingEnabled = false
//    }

}