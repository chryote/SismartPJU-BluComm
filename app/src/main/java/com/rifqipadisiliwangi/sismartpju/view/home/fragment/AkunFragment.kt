package com.rifqipadisiliwangi.sismartpju.view.home.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rifqipadisiliwangi.sismartpju.databinding.FragmentAkunBinding
import com.rifqipadisiliwangi.sismartpju.view.auth.LoginActivity

class AkunFragment : Fragment() {

    private lateinit var binding : FragmentAkunBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAkunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                .setTitle("Tutup Aplikasi")
                .setMessage("Ingin Keluar Aplikasi?")
                .setPositiveButton("Ya"){ _: DialogInterface, i: Int ->
                    logout()
                }
                .setNegativeButton("Tidak"){ dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
                .show()
        }
    }

    private fun logout() {
        val sharedPrefs = this.requireActivity()
            .getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.clear()
        editor.apply()
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}