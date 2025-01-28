package com.example.cinephile.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cinephile.R
import com.example.cinephile.databinding.FragmentMovieBinding
import com.example.cinephile.model.MovieModel
import com.example.cinephile.repository.MovieRepositoryImpl
import com.example.cinephile.ui.activity.utils.LoadingUtils
import com.example.cinephile.viewmodel.MovieViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MovieFragment : Fragment() {

    private lateinit var binding: FragmentMovieBinding
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var loadingUtils: LoadingUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieBinding.inflate(inflater, container, false)

        // Initialize ViewModel
        val repo = MovieRepositoryImpl()
        movieViewModel = MovieViewModel(repo)

        loadingUtils = LoadingUtils(requireActivity())

        // Handle Add Movie Button Click
        binding.addMoviebtn.setOnClickListener {
            val movieTitle = binding.MovieTitle.text.toString().trim()
            val movieYear = binding.MovieYear.text.toString().trim()
            val movieRuntime = binding.MovieRuntime.text.toString().trim()
            val movieSummary = binding.movieSummary.text.toString().trim()

            // Validate input
            if (movieTitle.isEmpty() || movieYear.isEmpty() || movieRuntime.isEmpty() || movieSummary.isEmpty()) {
                Toast.makeText(requireContext(), "All fields are required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Show loading spinner
            loadingUtils.show()

            // Create MovieModel instance
            val model = MovieModel("", movieTitle, movieYear, movieRuntime, movieSummary)

            // Add movie to database
            movieViewModel.addMovies(model) { success, message ->
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
