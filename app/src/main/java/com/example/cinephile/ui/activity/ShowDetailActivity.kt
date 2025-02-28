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
import com.example.cinephile.databinding.ActivityFilmDetailBinding
import com.example.cinephile.databinding.ActivityShowDetailBinding
import com.example.cinephile.repository.ShowRepositoryImpl
import com.example.cinephile.viewmodel.ShowViewModel
import eightbitlab.com.blurview.BlurView

class ShowDetailActivity : AppCompatActivity() {
    lateinit var showviemodel: ShowViewModel
    private lateinit var binding: ActivityShowDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showviemodel = ShowViewModel(ShowRepositoryImpl())

        binding = ActivityShowDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val blurView: BlurView = findViewById(R.id.showBlurView) // Assuming you have this in your layout

        val decorView = window.decorView

        // ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup

        val windowBackground = decorView.background
        blurView.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)

        // Rest of the code...
        val showId = intent.getStringExtra("showID")
        val showTitle = intent.getStringExtra("showTitle")
        val showDesc = intent.getStringExtra("showDesc")
        val showYear = intent.getStringExtra("showYear")
        val showImdb = intent.getStringExtra("showImdb")
        var showPic = intent.getStringExtra("showPic")

        binding.showDetailName.text = showTitle
        binding.showDetailSummary.text = showDesc
        binding.showDetailYear.text = showYear

        binding.updateshowbtn.setOnClickListener {
            val intent = Intent(this, UpdateShowActivity::class.java).apply {
                putExtra("showID", showId)
                putExtra("showTitle", showTitle)
                putExtra("showDesc", showDesc)
                putExtra("showYear", showYear)
                putExtra("showImdb", showImdb)
                putExtra("showPic", showPic)
            }
            startActivity(intent)
        }

        binding.deleteshowbtn.setOnClickListener{
            val showId = intent.getStringExtra("showID")
            if(showId != null){
                deleteShow(showId)
            } else {
                Toast.makeText(this, "Show ID is missing", Toast.LENGTH_SHORT).show()
            }
        }

        Glide.with(this).load(showPic).into(binding.showPic)

        binding.showDetailImdb.text = "IMDB: $showImdb"

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun deleteShow(showId: String) {
        showviemodel.deleteShow(showId) { success, message ->
            if (success) {
                Toast.makeText(this, "Show deleted successfully", Toast.LENGTH_SHORT).show()
                finish()  // Close the activity after deletion
            } else {
                Toast.makeText(this, "Failed to delete Show: $message", Toast.LENGTH_LONG).show()
            }
        }
    }
}
