package com.example.cinephile.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.cinephile.R
import com.example.cinephile.databinding.ActivityShowDetailBinding
import com.example.cinephile.model.ShowModel
import com.example.cinephile.repository.ShowRepositoryImpl
import com.example.cinephile.viewmodel.ShowViewModel
import eightbitlab.com.blurview.BlurView
import com.google.firebase.database.*

class ShowDetailActivity : AppCompatActivity() {

    lateinit var showViewModel: ShowViewModel
    private lateinit var binding: ActivityShowDetailBinding

    private val showReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Shows")
    private lateinit var currentShow: ShowModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showViewModel = ShowViewModel(ShowRepositoryImpl())
        binding = ActivityShowDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val blurView: BlurView = findViewById(R.id.showBlurView)
        val decorView = window.decorView
        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
        val windowBackground = decorView.background
        blurView.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)

        val showId = intent.getStringExtra("showID")
        val showTitle = intent.getStringExtra("showName")
        val showDesc = intent.getStringExtra("showSummary")
        val showYear = intent.getStringExtra("showYear")
        val showImdb = intent.getStringExtra("showImdb")
        val showPic = intent.getStringExtra("showPic")
        val showSeasons = intent.getIntExtra("showSeasons",20)
        val showEpisodes = intent.getIntExtra("showEpisodes",0)

        binding.showDetailName.text = showTitle
        binding.showDetailSummary.text = showDesc
        binding.showDetailYear.text = showYear
        Glide.with(this).load(showPic).into(binding.showPic)
        binding.showDetailImdb.text = "IMDB: $showImdb"
        binding.showDetailSeasons.text = "Number of Seasons: $showSeasons"
        binding.showDetailEpisodes.text = "Total Episodes: $showEpisodes"

        // Apply window insets for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.updateshowbtn.setOnClickListener {
            if (showId != null && showTitle != null && showDesc != null && showYear != null && showImdb != null) {
                val intent = Intent(this, UpdateShowActivity::class.java).apply {
                    putExtra("showID", currentShow.showId)
                    putExtra("showName", currentShow.showName)
                    putExtra("showSummary", currentShow.showSummary)
                    putExtra("showYear", currentShow.showYear)
                    putExtra("showImdb", currentShow.IMDB)
                    putExtra("showPic", currentShow.imageUrl)
                    putExtra("showSeasons",currentShow.showSeasons)
                    putExtra("showEpisodes",currentShow.showEpisodes)

                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Some show details are missing", Toast.LENGTH_SHORT).show()
            }
        }

        binding.deleteshowbtn.setOnClickListener {
            if (showId != null) {
                deleteShow(showId)
            } else {
                Toast.makeText(this, "Show ID is missing", Toast.LENGTH_SHORT).show()
            }
        }

        showId?.let {
            listenForShowChanges(it)
        }
    }

    private fun listenForShowChanges(showId: String) {
        showReference.child(showId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val updatedShow = snapshot.getValue(ShowModel::class.java)
                    updatedShow?.let {
                        currentShow = it
                        updateUI(it)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ShowDetailActivity, "Error listening for show updates: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI(show: ShowModel) {
        binding.showDetailName.text = show.showName
        binding.showDetailSummary.text = show.showSummary
        binding.showDetailYear.text = show.showYear
        binding.showDetailImdb.text = "IMDB: ${show.IMDB}"
        binding.showDetailSeasons.text = "Number of Seasons: ${show.showSeasons}"
        binding.showDetailEpisodes.text = "Total Episodes: ${show.showEpisodes}"
        Glide.with(this).load(show.imageUrl).into(binding.showPic)
    }

    override fun onDestroy() {
        super.onDestroy()
        showReference.removeEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // Function to delete the show from Firebase Realtime Database
    private fun deleteShow(showId: String) {
        showViewModel.deleteShow(showId) { success, message ->
            if (success) {
                Toast.makeText(this, "Show deleted successfully", Toast.LENGTH_SHORT).show()
                finish()  // Close the activity after deletion
            } else {
                Toast.makeText(this, "Failed to delete Show: $message", Toast.LENGTH_LONG).show()
            }
        }
    }
}
