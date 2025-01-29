package com.example.cinephile.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cinephile.R
import com.example.cinephile.databinding.FragmentShowBinding
import com.example.cinephile.model.MovieModel
import com.example.cinephile.model.ShowModel
import com.example.cinephile.repository.MovieRepositoryImpl
import com.example.cinephile.repository.ShowRepositoryImpl
import com.example.cinephile.ui.activity.utils.LoadingUtils
import com.example.cinephile.viewmodel.MovieViewModel
import com.example.cinephile.viewmodel.ShowViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ShowFragment : Fragment() {

    private lateinit var binding: FragmentShowBinding
    private lateinit var showViewModel: ShowViewModel
    private lateinit var loadingUtils: LoadingUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowBinding.inflate(inflater, container, false)

        // Initialize ViewModel
        val repo = ShowRepositoryImpl()
        showViewModel = ShowViewModel(repo)

        loadingUtils = LoadingUtils(requireActivity())

        // Handle Add Movie Button Click
        binding.addShowBtn.setOnClickListener {
            val showTitle = binding.ShowTitle.text.toString().trim()
            val showYear = binding.ShowYear.text.toString().trim()
            val showSeasons = binding.ShowSeasons.text.toString().toInt()
            val showEpisodes = binding.ShowEpisodes.text.toString().toInt()
            val showSummary = binding.ShowSummary.text.toString().trim()
            val showImdb = binding.IMDBInput.text.toString().trim()


            // Validate input
            if (showTitle.isEmpty() || showYear.isEmpty() || showSummary.isEmpty() || showSeasons.toString().isEmpty() || showEpisodes.toString().isEmpty()) {
                Toast.makeText(requireContext(), "All fields are required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Show loading spinner
            loadingUtils.show()

            // Create MovieModel instance
            val model = ShowModel("", showTitle,showYear, showSeasons,showEpisodes,showSummary, showImdb )

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

        return binding.root
    }
}
