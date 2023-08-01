package com.rifqipadisiliwangi.sismartpju.view.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.rifqipadisiliwangi.sismartpju.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rifqipadisiliwangi.sismartpju.databinding.ActivityDashboardBinding
import com.rifqipadisiliwangi.sismartpju.view.home.fragment.AkunFragment
import com.rifqipadisiliwangi.sismartpju.view.home.fragment.HomeFragment
import com.rifqipadisiliwangi.sismartpju.view.home.fragment.PekerjaanFragment
import com.rifqipadisiliwangi.sismartpju.view.home.fragment.PerangkatFragment

class DashboardActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener  {

    private lateinit var binding : ActivityDashboardBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var pekerjaanFragment: PekerjaanFragment
    private lateinit var perangkatFragment: PerangkatFragment
    private lateinit var akunFragment: AkunFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this)


        homeFragment =  HomeFragment()
        pekerjaanFragment = PekerjaanFragment()
        perangkatFragment = PerangkatFragment()
        akunFragment = AkunFragment()
        binding.bottomNavigationView.selectedItemId = R.id.menu_item_1
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val transaction = supportFragmentManager.beginTransaction()
        when (item.itemId) {
            R.id.menu_item_1 -> {
                transaction.replace(R.id.fragmentContainer, homeFragment)
                    .addToBackStack(null) // Add the fragment to the back stack
                    .commit()
                return true
            }
            R.id.menu_item_2 -> {
                transaction.replace(R.id.fragmentContainer, pekerjaanFragment)
                    .addToBackStack(null) // Add the fragment to the back stack
                    .commit()
                return true
            }
            R.id.menu_item_3 -> {
                transaction.replace(R.id.fragmentContainer, perangkatFragment)
                    .addToBackStack(null)
                    .commit()
                return true
            }
            R.id.menu_item_4 -> {
                transaction.replace(R.id.fragmentContainer, akunFragment)
                    .addToBackStack(null) // Add the fragment to the back stack
                    .commit()
                return true
            }
        }
        return false
    }

    override fun onBackPressed() {
        // Handle the back press event
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack() // Pop the fragment from the back stack
        } else {
            super.onBackPressed() // Perform the default back button behavior
        }
    }
}


