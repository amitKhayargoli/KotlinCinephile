package com.example.cinephile.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.cinephile.R
import com.example.cinephile.databinding.ActivityFilmDetailBinding
import com.example.cinephile.repository.MovieRepositoryImpl
import com.example.cinephile.viewmodel.MovieViewModel
import eightbitlab.com.blurview.BlurView


class FilmDetailActivity : AppCompatActivity() {

    lateinit var movieViewModel: MovieViewModel
    private lateinit var binding: ActivityFilmDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        movieViewModel = MovieViewModel(MovieRepositoryImpl())

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


        binding.editFilmButton.setOnClickListener {
            val intent = Intent(this, UpdateMovieActivity::class.java).apply {
                putExtra("movieID", movieId)
                putExtra("movieTitle", movieTitle)
                putExtra("movieDesc", movieDesc)
                putExtra("movieYear", movieYear)
                putExtra("movieImdb", movieImdb)
                putExtra("filmPic", moviePic)
                putExtra("movieRuntime", movieRuntime)
            }
            startActivity(intent)
        }



        binding.deleteFilmButton.setOnClickListener {
            // Handle the deletion logic here
            val movieId = intent.getStringExtra("movieID")

            if (movieId != null) {
                deleteMovie(movieId)
            } else {
                Toast.makeText(this, "Movie ID is missing", Toast.LENGTH_SHORT).show()
            }
        }




        Glide.with(this).load(moviePic).into(binding.filmPic)

        binding.FilmDetailImdb.text = "IMDB: $movieImdb"
        binding.MovieRuntime.text = "Runtime: $movieRuntime minutes"



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun deleteMovie(movieId: String) {

        movieViewModel.deleteMovie(movieId) { success, message ->
            if (success) {
                Toast.makeText(this, "Movie deleted successfully", Toast.LENGTH_SHORT).show()
                finish()  // Close the activity after deletion
            } else {
                Toast.makeText(this, "Failed to delete movie: $message", Toast.LENGTH_LONG).show()
            }
        }
    }

}