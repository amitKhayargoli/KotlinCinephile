package com.example.cinephile.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cinephile.R
import com.example.cinephile.databinding.ActivityAddMoviesBinding
import com.example.cinephile.databinding.ActivityLoginBinding
import com.github.dhaval2404.imagepicker.ImagePicker

class AddMovies : AppCompatActivity() {
    lateinit var binding:ActivityAddMoviesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView2.setOnClickListener{
            ImagePicker.with(this)
                .crop()  // Enable cropping
                .compress(1024)  // Compress image to 1MB
                .maxResultSize(1080, 1080)  // Max image size
                .start()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}