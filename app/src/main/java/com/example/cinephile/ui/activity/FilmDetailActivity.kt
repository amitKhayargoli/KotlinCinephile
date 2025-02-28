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
import com.example.cinephile.model.MovieModel
import com.example.cinephile.repository.MovieRepositoryImpl
import com.example.cinephile.viewmodel.MovieViewModel
import eightbitlab.com.blurview.BlurView
import com.google.firebase.database.*


class FilmDetailActivity : AppCompatActivity() {

    lateinit var movieViewModel: MovieViewModel
    private lateinit var binding: ActivityFilmDetailBinding
    private lateinit var movieReference: DatabaseReference  // Firebase reference
    private lateinit var currentMovie: MovieModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieViewModel = MovieViewModel(MovieRepositoryImpl())
        binding = ActivityFilmDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val blurView: BlurView = findViewById(R.id.filmBlurView)

        val decorView = window.decorView
        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
        val windowBackground = decorView.background
        blurView.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)

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

        // Setup Firebase reference for real-time updates
        movieId?.let {
            movieReference = FirebaseDatabase.getInstance().getReference("Movies").child(it)
            listenForMovieChanges()
        }

        binding.editFilmButton.setOnClickListener {
            val intent = Intent(this, UpdateMovieActivity::class.java).apply {
                // Use the updated values from Firebase
                putExtra("movieID", currentMovie.movieId)
                putExtra("movieTitle", currentMovie.movieName)
                putExtra("movieDesc", currentMovie.movieSummary)
                putExtra("movieYear", currentMovie.movieYear)
                putExtra("movieImdb", currentMovie.IMDB)
                putExtra("filmPic", currentMovie.imageUrl)
                putExtra("movieRuntime", currentMovie.movieRuntime)
            }
            startActivity(intent)
        }

        binding.deleteFilmButton.setOnClickListener {
            // Handle the deletion logic here
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

    private fun listenForMovieChanges() {
        // Listen for changes to the movie data in real-time
        movieReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val updatedMovie = snapshot.getValue(MovieModel::class.java)
                updatedMovie?.let {
                    currentMovie = it  // Store the updated movie data
                    updateUI(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@FilmDetailActivity, "Failed to fetch movie data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI(movie: MovieModel) {
        // Update the UI with new movie data
        binding.FilmDetailName.text = movie.movieName
        binding.FilmDetailSummary.text = movie.movieSummary
        binding.FilmDetailYear.text = movie.movieYear
        binding.FilmDetailImdb.text = "IMDB: ${movie.IMDB}"
        binding.MovieRuntime.text = "Runtime: ${movie.movieRuntime} minutes"
        Glide.with(this).load(movie.imageUrl).into(binding.filmPic)
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

    override fun onDestroy() {
        super.onDestroy()
        // Remove the Firebase listener when the activity is destroyed to avoid memory leaks
        movieReference.removeEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
