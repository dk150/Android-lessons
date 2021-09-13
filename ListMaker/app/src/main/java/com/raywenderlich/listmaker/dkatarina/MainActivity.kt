package com.raywenderlich.listmaker.dkatarina

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.raywenderlich.listmaker.dkatarina.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        title = getString(R.string.app_name)
    }

}