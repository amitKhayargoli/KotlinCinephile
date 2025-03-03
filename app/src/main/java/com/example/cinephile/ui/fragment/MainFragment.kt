package com.example.cinephile.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.cinephile.Adapter.MovieListAdapter
import com.example.cinephile.Adapter.ShowListAdapter
import com.example.cinephile.Adapter.SliderAdapter
import com.example.cinephile.R
import com.example.cinephile.databinding.FragmentHomeBinding
import com.example.cinephile.model.MovieModel
import com.example.cinephile.model.ShowModel
import com.example.cinephile.model.SliderItems
import com.google.firebase.database.*

class MainFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    private lateinit var movieListAdapter: MovieListAdapter
    private val movieItems = ArrayList<MovieModel>()

    private lateinit var showListAdapter: ShowListAdapter
    private val showItems = ArrayList<ShowModel>()

    private val sliderHandler = Handler()
    private val sliderRunnable = Runnable {
        binding.viewPager2.currentItem = binding.viewPager2.currentItem + 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()

        initBanner()
        initMovies()
        initShows()
    }

    private fun setupRecyclerViews() {
        movieListAdapter = MovieListAdapter(requireContext(), movieItems)
        binding.recyclerViewTopMovies.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )
        binding.recyclerViewTopMovies.adapter = movieListAdapter

        showListAdapter = ShowListAdapter(requireContext(), showItems)
        binding.recyclerViewUpcoming.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )
        binding.recyclerViewUpcoming.adapter = showListAdapter
    }

    private fun initMovies() {
        val reference: DatabaseReference = database.reference.child("Movies")
        binding.progressBarTopMovies.visibility = View.VISIBLE

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                movieItems.clear()

                if (snapshot.exists()) {
                    for (issue in snapshot.children) {
                        val movie = issue.getValue(MovieModel::class.java)
                        movie?.let { movieItems.add(it) }
                    }
                    movieListAdapter.notifyDataSetChanged()
                }
                binding.progressBarTopMovies.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun initShows() {
        val reference: DatabaseReference = database.reference.child("Shows")
        binding.progressBarUpcoming.visibility = View.VISIBLE

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                showItems.clear()

                if (snapshot.exists()) {
                    for (issue in snapshot.children) {
                        val show = issue.getValue(ShowModel::class.java)
                        show?.let { showItems.add(it) }
                    }
                    showListAdapter.notifyDataSetChanged()
                }
                binding.progressBarUpcoming.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun initBanner() {
        binding.progressBarSlider.visibility = View.VISIBLE

        val lists = mutableListOf(
            SliderItems(image = R.drawable.cap),
            SliderItems(image = R.drawable.dune),
            SliderItems(image = R.drawable.darkknight),
            SliderItems(image = R.drawable.fightclub),
            SliderItems(image = R.drawable.johnwick),
            SliderItems(image = R.drawable.wide)
        )

        binding.progressBarSlider.visibility = View.GONE
        banners(lists)
    }

    private fun banners(lists: MutableList<SliderItems>) {
        binding.viewPager2.adapter = SliderAdapter(lists, binding.viewPager2)
        binding.viewPager2.clipToPadding = false
        binding.viewPager2.clipChildren = false
        binding.viewPager2.offscreenPageLimit = 3
        binding.viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
            addTransformer(ViewPager2.PageTransformer { page, position ->
                val r = 1 - Math.abs(position)
                page.scaleY = 0.85f + r * 0.15f
            })
        }

        binding.viewPager2.setPageTransformer(compositePageTransformer)
        binding.viewPager2.currentItem = 1
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 2000)
    }
}
