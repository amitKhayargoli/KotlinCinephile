package com.example.cinephile.ui.activity

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

class ShowDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilmDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityFilmDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)


        val showId = intent.getStringExtra("showID")
        val showTitle = intent.getStringExtra("showTitle")
        val showDesc = intent.getStringExtra("showDesc")
        val showYear = intent.getStringExtra("showYear")
        val showImdb = intent.getStringExtra("showImdb")
        var showPic = intent.getStringExtra("showPic")



        binding.FilmDetailName.text = showTitle
        binding.FilmDetailSummary.text = showDesc
        binding.FilmDetailYear.text = showYear



        Glide.with(this).load(showPic).into(binding.filmPic)

        binding.FilmDetailImdb.text = "IMDB: $showImdb"

        binding.progressBar2.visibility = View.GONE


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}