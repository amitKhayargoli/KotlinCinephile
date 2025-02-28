package com.example.cinephile.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.cinephile.R
import com.example.cinephile.databinding.ActivityFilmDetailBinding
import eightbitlab.com.blurview.BlurView


class FilmDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilmDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityFilmDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val blurView: BlurView = findViewById(R.id.filmBlurView)

        val decorView = window.decorView

        // ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup

        val windowBackground = decorView.background
        blurView.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)

//            .setBlurRadius(0.1f)


        val movieId = intent.getStringExtra("movieID")
        val movieTitle = intent.getStringExtra("movieTitle")
        val movieDesc = intent.getStringExtra("movieDesc")
        val movieYear = intent.getStringExtra("movieYear")
        val movieImdb = intent.getStringExtra("movieImdb")
        var moviePic = intent.getStringExtra("filmPic")
        var movieRuntime = intent.getStringExtra("movieRuntime")



        binding.FilmDetailName.text = movieTitle
        binding.FilmDetailSummary.text = movieDesc
        binding.FilmDetailYear.text = movieYear



        Glide.with(this).load(moviePic).into(binding.filmPic)

        binding.FilmDetailImdb.text = "IMDB: $movieImdb"



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}