package com.instiper.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.instiper.myapplication.R
import com.instiper.myapplication.databinding.ActivityMainBinding
import com.instiper.myapplication.ui.addiot.AddIotFragment
import com.instiper.myapplication.ui.home.HomeFragment
import com.instiper.myapplication.ui.info.InfoFragment
import com.instiper.myapplication.ui.profile.ProfileFragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

       val navView = findViewById<ChipNavigationBar>(R.id.nav_view)
       navView.setItemSelected(R.id.home, true)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, HomeFragment()).commit()
        bottomMenu(navView)
    }

    private fun bottomMenu(navView: ChipNavigationBar?) {
        navView?.setOnItemSelectedListener { i: Int ->
            var fragment : Fragment? = null
            when(i){
                R.id.home -> fragment = HomeFragment()
                R.id.profile -> fragment = ProfileFragment()
                R.id.info -> fragment = InfoFragment()
            }

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment!!)
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}