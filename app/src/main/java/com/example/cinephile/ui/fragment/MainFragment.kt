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
import com.example.cinephile.repository.UserRepository
import com.example.cinephile.repository.UserRepositoryImpl
import com.example.cinephile.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainFragment : Fragment() {

//        private var _binding: FragmentHomeBinding? = null
//        private val binding get() = _binding!!

    val database: FirebaseDatabase =
        FirebaseDatabase.getInstance()



    lateinit var binding: FragmentHomeBinding
    lateinit var userRepositoryImpl: UserRepositoryImpl
    val auth = FirebaseAuth.getInstance()


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

        userRepositoryImpl = UserRepositoryImpl()
        fetchUserData()
        return binding.root
    }

    fun fetchUserData(){
        val userId = auth.currentUser?.uid

        if(userId!=null){
            userRepositoryImpl.getUserFromDatabase(userId){
                    user,success,message-> if(success && user!=null){
                val emailPrefix = user.email.split("@")[0]
                binding.DashName.text = "Welcome $emailPrefix"
                binding.DashEmail.text = "${user.email}"
            }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize banner
        initBanner()
        initMovies()
        initShows()
    }

    private fun initBanner() {
        binding.progressBarSlider.visibility = View.VISIBLE

        val lists = mutableListOf<SliderItems>()
        lists.add(SliderItems(image = R.drawable.cap))
        lists.add(SliderItems(image = R.drawable.dune))
        lists.add(SliderItems(image = R.drawable.darkknight))
        lists.add(SliderItems(image = R.drawable.fightclub))
        lists.add(SliderItems(image = R.drawable.johnwick))
        lists.add(SliderItems(image = R.drawable.wide))

        binding.progressBarSlider.visibility = View.GONE
        banners(lists)
    }

    private fun initMovies(){
        val reference: DatabaseReference = database.reference.child("Movies")
        binding.progressBarTopMovies.visibility = View.VISIBLE
        val items = ArrayList<MovieModel>()

        reference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(issue in snapshot.children){
                        items.add(issue.getValue(MovieModel::class.java)!!)

                    }
                    if(items.isNotEmpty()){
                        binding.recyclerViewTopMovies.layoutManager = LinearLayoutManager(
                            requireContext(),LinearLayoutManager.HORIZONTAL,
                            false
                        )

                        binding.recyclerViewTopMovies.adapter = MovieListAdapter(requireContext(),items)

                    }
                    binding.progressBarTopMovies.visibility = View.GONE

                }
            }

            override fun onCancelled(error: DatabaseError) {


            }

        })

    }

    private fun initShows(){
        binding.progressBarUpcoming.visibility = View.VISIBLE
        val items = ArrayList<ShowModel>()
        val reference: DatabaseReference = database.reference.child("Shows")
        reference.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(issue in snapshot.children){
                        items.add(issue.getValue(ShowModel::class.java)!!)

                    }
                    if(items.isNotEmpty()){
                        binding.recyclerViewUpcoming.layoutManager = LinearLayoutManager(
                            requireContext(),LinearLayoutManager.HORIZONTAL,
                            false
                        )

                        binding.recyclerViewUpcoming.adapter = ShowListAdapter(requireContext(),items)

                    }
                    binding.progressBarUpcoming.visibility = View.GONE

                }
            }

            override fun onCancelled(error: DatabaseError) {


            }

        })

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

    override fun onDestroyView() {
        super.onDestroyView()
//        binding = null
    }
}