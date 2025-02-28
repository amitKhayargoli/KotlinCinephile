package com.example.cinephile.ui.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cinephile.R
import com.example.cinephile.databinding.ActivityAddMoviesBinding
import com.example.cinephile.databinding.ActivityLoginBinding
import com.example.cinephile.ui.activity.utils.ImageUtils
import com.example.cinephile.viewmodel.ImageViewModel
import com.example.cinephile.viewmodel.MovieViewModel
import com.github.dhaval2404.imagepicker.ImagePicker

class AddMovies : AppCompatActivity() {
    lateinit var binding:ActivityAddMoviesBinding

    lateinit var imageUtils: ImageUtils

    var imageUri: Uri? = null

    lateinit var imageViewModel: ImageViewModel

    lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }



}