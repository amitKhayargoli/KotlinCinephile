package com.example.cinephile.ui.fragment

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.cinephile.databinding.FragmentMovieBinding
import com.example.cinephile.model.MovieModel
import com.example.cinephile.repository.ImageRepositoryImpl
import com.example.cinephile.repository.MovieRepositoryImpl
import com.example.cinephile.ui.activity.utils.ImageUtils
import com.example.cinephile.ui.activity.utils.LoadingUtils
import com.example.cinephile.viewmodel.ImageViewModel
import com.example.cinephile.viewmodel.MovieViewModel
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MovieFragment : Fragment() {

    private lateinit var binding: FragmentMovieBinding
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var loadingUtils: LoadingUtils
    private lateinit var imageViewModel: ImageViewModel
    private lateinit var imageUtils: ImageUtils

    private var imageUri: Uri? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // ✅ Register the image picker launcher before the fragment starts
        val imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val imageData = result.data
                Log.d("ImageUtils", "Result Code: ${result.resultCode}, Intent Data: $imageData")

                if (result.resultCode == Activity.RESULT_OK && imageData != null) {
                    val uri = imageData.data
                    Log.d("ImageUtils", "Selected Image URI: $uri")

                    if (uri != null) {
                        imageUri = uri
                        Picasso.get().load(uri).into(binding.imageBrowse)
                    } else {
                        Log.e("ImageUtils", "Image URI is null!")
                    }
                } else {
                    Log.e("ImageUtils", "Image selection failed or cancelled")
                }
            }


        // ✅ Register permission launcher before the fragment starts
        val permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    imageUtils.launchGallery(requireContext()) // Launch gallery if permission granted
                } else {
                    Log.e("ImageUtils", "Permission denied for gallery access")
                }
            }

        // ✅ Pass pre-registered launchers to ImageUtils
        imageUtils = ImageUtils(imagePickerLauncher, permissionLauncher) { uri ->
            imageUri = uri
            uri?.let { Picasso.get().load(it).into(binding.imageBrowse) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieBinding.inflate(inflater, container, false)

        imageViewModel = ImageViewModel(ImageRepositoryImpl())
        movieViewModel = MovieViewModel(MovieRepositoryImpl())

        binding.imageBrowse.setOnClickListener {
            imageUtils.launchGallery(requireContext()) // Launch gallery on button click
        }

        loadingUtils = LoadingUtils(requireActivity())

        binding.addMoviebtn.setOnClickListener {
            uploadImage()
        }

        return binding.root
    }

    private fun uploadImage() {
        imageUri?.let { uri ->
            imageViewModel.uploadImage(requireContext(), uri) { imageUrl ->
                if (imageUrl != null) {
                    addMovie(imageUrl)
                } else {
                    Log.e("Upload Error", "Failed to upload image to Cloudinary")
                }
            }
        }
    }

    private fun addMovie(url: String) {
        val movieTitle = binding.MovieTitle.text.toString().trim()
        val movieYear = binding.MovieYear.text.toString().trim()
        val movieRuntime = binding.MovieRuntime.text.toString().trim()
        val movieSummary = binding.movieSummary.text.toString().trim()
        val IMDB = binding.IMDBInput.text.toString().trim()

        if (movieTitle.isEmpty() || movieYear.isEmpty() || movieRuntime.isEmpty() || movieSummary.isEmpty()) {
            Toast.makeText(requireContext(), "All fields are required!", Toast.LENGTH_SHORT).show()
            return
        }

        loadingUtils.show()

        val model = MovieModel("", movieTitle, movieYear, movieRuntime, movieSummary, IMDB, url)

        movieViewModel.addMovies(model) { success, message ->
            loadingUtils.dismiss()
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }
}

