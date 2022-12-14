package com.example.tubes1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.tubes1.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView


class MainMenuActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val profileFragment = ProfileFragment()
    private val helpFragment = HelpFragment()
    private val deliveryFragment = DeliveryFragment()
    private val gudangFragment = GudangFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        supportActionBar?.hide()

        replaceFragment(homeFragment)
        val bottom: NavigationBarView = findViewById(R.id.bottom_navigation)
        bottom.setOnItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> replaceFragment(homeFragment)
                R.id.ic_profile->replaceFragment(profileFragment)
                R.id.ic_call->replaceFragment(helpFragment)
                R.id.ic_deliv->replaceFragment(deliveryFragment)
                R.id.ic_gudang->replaceFragment(gudangFragment)
            }
            true
        }
    }

        private fun replaceFragment(fragment: Fragment) {
            if (fragment != null) {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, fragment)
                transaction.commit()
            }
        }

    }

