package com.rifqipadisiliwangi.sismartpju.view.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rifqipadisiliwangi.sismartpju.R
import com.rifqipadisiliwangi.sismartpju.databinding.FragmentAkunBinding

class AkunFragment : Fragment() {

    private lateinit var binding : FragmentAkunBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAkunBinding.inflate(inflater, container, false)
        return binding.root
    }
}