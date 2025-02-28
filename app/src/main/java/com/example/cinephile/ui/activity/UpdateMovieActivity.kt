package com.example.cinephile.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.cinephile.R
import com.example.cinephile.repository.MovieRepositoryImpl
import com.example.cinephile.viewmodel.MovieViewModel
class UpdateMovieActivity : AppCompatActivity() {

    // Initialize the MovieViewModel using viewModels() to get an instance

    private lateinit var movieViewModel: MovieViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_movie)
        movieViewModel = MovieViewModel(MovieRepositoryImpl())


        // Getting the data from the intent
        val movieId = intent.getStringExtra("movieID")
        val movieTitle = intent.getStringExtra("movieTitle")
        val movieDesc = intent.getStringExtra("movieDesc")
        val movieYear = intent.getStringExtra("movieYear")
        val movieImdb = intent.getStringExtra("movieImdb")
        val moviePic = intent.getStringExtra("filmPic")
        val movieRuntime = intent.getStringExtra("movieRuntime")

        // Initialize views
        val titleEditText: EditText = findViewById(R.id.MovieTitle)
        val descEditText: EditText = findViewById(R.id.movieSummary)
        val yearTextView: TextView = findViewById(R.id.MovieYear)
        val imdbTextView: TextView = findViewById(R.id.IMDBInput)
        val runtimeTextView: TextView = findViewById(R.id.MovieRuntime)
        val moviePicImageView: ImageView = findViewById(R.id.imageBrowse)

        // Set the data to the views
        titleEditText.setText(movieTitle)
        descEditText.setText(movieDesc)
        yearTextView.text = movieYear
        imdbTextView.text = "IMDB: $movieImdb"
        runtimeTextView.text = movieRuntime
        Glide.with(this).load(moviePic).into(moviePicImageView)

        // Adjust padding to handle edge to edge behavior
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up the update button and its click listener
        val updateButton: Button = findViewById(R.id.updateMovieBtn)
        updateButton.setOnClickListener {

            // Collect updated data from the views
            val updatedTitle = titleEditText.text.toString()
            val updatedDesc = descEditText.text.toString()
            val updatedYear = yearTextView.text.toString()
            val updatedImdb = imdbTextView.text.toString().removePrefix("IMDB: ")
            val updatedRuntime = runtimeTextView.text.toString()

            // Create a mutable map of updated movie data
            val updatedData = mutableMapOf<String, Any>(
                "movieName" to updatedTitle,
                "description" to updatedDesc,
                "movieYear" to updatedYear,
                "imdb" to updatedImdb,
                "movieRuntime" to updatedRuntime
            )

            // Call the updateMovie function from the ViewModel
            movieId?.let {
                movieViewModel.updateMovie(it, updatedData) { success, message ->
                    if (success) {
                        finish()

                    } else {
                        // Handle failure (maybe show an error message)
                        Toast.makeText(this, "Failed to update movie: $message", Toast.LENGTH_LONG).show()

                    }
                }
            }
        }
    }
}
