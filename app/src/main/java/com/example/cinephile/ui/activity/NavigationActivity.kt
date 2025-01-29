package com.example.cinephile.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cinephile.R
import com.example.cinephile.databinding.ActivityNavigationBinding
import com.example.cinephile.ui.fragment.AboutFragment
import com.example.cinephile.ui.fragment.MainFragment
import com.example.cinephile.ui.fragment.MovieFragment
import com.example.cinephile.ui.fragment.ShowFragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class NavigationActivity : AppCompatActivity() {

    private lateinit var navigationBinding: ActivityNavigationBinding


    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        navigationBinding = ActivityNavigationBinding.inflate(layoutInflater)

        setContentView(navigationBinding.root)
        replaceFragment(MainFragment())

        val chipNavigationBar = findViewById<ChipNavigationBar>(R.id.buttomNavigation)


        chipNavigationBar.setOnItemSelectedListener { id ->
            when (id) {
                R.id.explorer -> replaceFragment(MainFragment())
                R.id.profile -> replaceFragment(AboutFragment())
                R.id.movies -> replaceFragment(MovieFragment())
        R.id.Shows -> replaceFragment(ShowFragment())



                else -> false
            }
            true
        }


    }
    }
