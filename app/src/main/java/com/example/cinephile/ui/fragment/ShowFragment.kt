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
import com.example.cinephile.R
import com.example.cinephile.databinding.FragmentShowBinding
import com.example.cinephile.model.MovieModel
import com.example.cinephile.model.ShowModel
import com.example.cinephile.repository.ImageRepositoryImpl
import com.example.cinephile.repository.MovieRepositoryImpl
import com.example.cinephile.repository.ShowRepositoryImpl
import com.example.cinephile.ui.activity.utils.ImageUtils
import com.example.cinephile.ui.activity.utils.LoadingUtils
import com.example.cinephile.viewmodel.ImageViewModel
import com.example.cinephile.viewmodel.MovieViewModel
import com.example.cinephile.viewmodel.ShowViewModel
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ShowFragment : Fragment() {

    private lateinit var binding: FragmentShowBinding
    private lateinit var showViewModel: ShowViewModel
    private lateinit var imageViewModel: ImageViewModel
    private lateinit var imageUtils: ImageUtils

    private lateinit var loadingUtils: LoadingUtils

    private var imageUri: Uri? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

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
                    imageUtils.launchGallery(requireContext())
                } else {
                    Log.e("ImageUtils", "Permission denied for gallery access")
                }
            }

        imageUtils = ImageUtils(imagePickerLauncher, permissionLauncher) { uri ->
            imageUri = uri
            uri?.let { Picasso.get().load(it).into(binding.imageBrowse) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowBinding.inflate(inflater, container, false)

        // Initialize ViewModel
        val repo = ShowRepositoryImpl()
        imageViewModel = ImageViewModel(ImageRepositoryImpl())

        showViewModel = ShowViewModel(repo)

        binding.imageBrowse.setOnClickListener{
            imageUtils.launchGallery(requireContext())

        }

        loadingUtils = LoadingUtils(requireActivity())

        // Handle Add Movie Button Click

        binding.addShowBtn.setOnClickListener{
            uploadImage()
        }


        return binding.root
    }

    private fun uploadImage() {
        imageUri?.let { uri ->
            imageViewModel.uploadImage(requireContext(), uri) { imageUrl ->
                if (imageUrl != null) {
                    addShow(imageUrl)
                } else {
                    Log.e("Upload Error", "Failed to upload image to Cloudinary")
                }
            }
        }
    }

    private fun addShow(url:String){
        val showTitle = binding.ShowTitle.text.toString().trim()
        val showYear = binding.ShowYear.text.toString().trim()
        val showSeasons = binding.ShowSeasons.text.toString().toInt()
        val showEpisodes = binding.ShowEpisodes.text.toString().toInt()
        val showSummary = binding.ShowSummary.text.toString().trim()
        val showImdb = binding.IMDBInput.text.toString().trim()


        // Validate input
        if (showTitle.isEmpty() || showYear.isEmpty() || showSummary.isEmpty() || showSeasons.toString().isEmpty() || showEpisodes.toString().isEmpty()) {
            Toast.makeText(requireContext(), "All fields are required!", Toast.LENGTH_SHORT).show()
            return
        }

        // Show loading spinner
        loadingUtils.show()

        // Create MovieModel instance
        val model = ShowModel("", showTitle,showYear, showSeasons,showEpisodes,showSummary, showImdb ,url)

        // Add movie to database
        showViewModel.addShow(model) { success, message ->
            loadingUtils.dismiss()
            if (success) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
