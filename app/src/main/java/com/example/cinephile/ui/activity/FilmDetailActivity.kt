package com.example.cinephile.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.cinephile.R
import com.example.cinephile.databinding.ActivityFilmDetailBinding

class FilmDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilmDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityFilmDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)




        val movieId = intent.getStringExtra("movieID")
        val movieTitle = intent.getStringExtra("movieTitle")
        val movieDesc = intent.getStringExtra("movieDesc")
        val movieYear = intent.getIntExtra("movieYear", 0)
        val movieImdb = intent.getStringExtra("movieImdb")
        var moviePic = intent.getStringExtra("filmPic")

        binding.FilmDetailName.text = movieTitle
        binding.FilmDetailSummary.text = movieDesc
//        binding.FilmDetailYear.text = movieYear

//        binding.filmPic = moviePic
//        Glide.with(this).load(moviePic).into(binding.filmPic)

//        Glide.with(this).load(moviePic).into(binding.filmPic)

        binding.FilmDetailImdb.text = "IMDB: $movieImdb"



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}