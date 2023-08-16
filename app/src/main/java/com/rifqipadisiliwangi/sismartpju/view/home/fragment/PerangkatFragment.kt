package com.rifqipadisiliwangi.sismartpju.view.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rifqipadisiliwangi.sismartpju.databinding.FragmentPerangkatBinding
import com.rifqipadisiliwangi.sismartpju.view.daftar.controller.DaftarControllerActivity
import com.rifqipadisiliwangi.sismartpju.view.daftar.coordinator.DaftarCoordinatorActivity
import com.rifqipadisiliwangi.sismartpju.view.pairing.pairingdua.PairingDuaActivity
import com.rifqipadisiliwangi.sismartpju.view.pairing.pairingsatu.AddPairingActivity
import com.rifqipadisiliwangi.sismartpju.view.pairing.pairingsatu.PairingSatuActivity

class PerangkatFragment : Fragment() {

    private lateinit var binding : FragmentPerangkatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPerangkatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.llModeBluetooth.setOnClickListener {
            startActivity(Intent(context, AddPairingActivity::class.java))
        }

        binding.llModeKontroller.setOnClickListener {
            startActivity(Intent(context, PairingDuaActivity::class.java))
        }

        binding.llDaftarKontroller.setOnClickListener {
            startActivity(Intent(context, DaftarControllerActivity::class.java))
        }

        binding.llDaftarKoordinator.setOnClickListener {
            startActivity(Intent(context, DaftarCoordinatorActivity::class.java))
        }
    }

}