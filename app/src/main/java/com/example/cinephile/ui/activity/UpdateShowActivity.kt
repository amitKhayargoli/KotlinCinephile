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
import com.example.cinephile.repository.ShowRepositoryImpl
import com.example.cinephile.viewmodel.ShowViewModel

class UpdateShowActivity : AppCompatActivity() {

    private lateinit var showViewModel: ShowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_show)  // Make sure this layout exists in your project
        showViewModel = ShowViewModel(ShowRepositoryImpl())

        // Retrieve the data passed through the Intent
        val showId = intent.getStringExtra("showID")
        val showName = intent.getStringExtra("showName")
        val showSummary = intent.getStringExtra("showSummary")
        val showYear = intent.getStringExtra("showYear")
        val imdbRating = intent.getStringExtra("showImdb")
        val showImageUrl = intent.getStringExtra("showPic")
        val totalEpisodes = intent.getIntExtra("showEpisodes",12)
        val totalSeasons = intent.getIntExtra("showSeasons",2)



        // Binding views to variables
        val titleEditText: EditText = findViewById(R.id.ShowTitle)
        val summaryEditText: EditText = findViewById(R.id.ShowSummary)
        val yearTextView: TextView = findViewById(R.id.ShowYear)
        val imdbTextView: TextView = findViewById(R.id.IMDBInput)
        val showPicImageView: ImageView = findViewById(R.id.imageBrowse)
        val showSeasons : TextView = findViewById(R.id.ShowSeasons)
        val showEpisodes : TextView = findViewById(R.id.ShowEpisodes)


        // Pre-fill the fields with the current data
        titleEditText.setText(showName)
        summaryEditText.setText(showSummary)
        yearTextView.text = showYear
        imdbTextView.text = "IMDB: $imdbRating"
        showEpisodes.text = totalEpisodes.toString()
        showSeasons.text = totalSeasons.toString()


        Glide.with(this).load(showImageUrl).into(showPicImageView)

        // Adjust system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Update button logic
        val updateButton: Button = findViewById(R.id.updateShowButton)
        updateButton.setOnClickListener {

            // Get the updated values from the user input
            val updatedName = titleEditText.text.toString()
            val updatedSummary = summaryEditText.text.toString()
            val updatedYear = yearTextView.text.toString()
            val updatedImdb = imdbTextView.text.toString().removePrefix("IMDB: ")
            val updatedSeasons = showSeasons.text.toString().toInt()
            val updatedEpisodes = showEpisodes.text.toString().toInt()

            // Prepare data for updating the show
            val updatedData = mutableMapOf<String, Any>(
                "showName" to updatedName,
                "showSummary" to updatedSummary,
                "showYear" to updatedYear,
                "IMDB" to updatedImdb,
                "showSeasons" to updatedSeasons,
                "showEpisodes" to updatedEpisodes
            )

            // Update the show using the show ID
            showId?.let {
                showViewModel.updateShow(it, updatedData) { success, message ->
                    if (success) {
                        Toast.makeText(this, "Show updated successfully", Toast.LENGTH_SHORT).show()
                        finish()  // Close the activity after update
                    } else {
                        Toast.makeText(this, "Failed to update show: $message", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
