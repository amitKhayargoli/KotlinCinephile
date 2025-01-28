package com.example.cinephile.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.cinephile.Adapter.SliderAdapter
import com.example.cinephile.R
import com.example.cinephile.databinding.FragmentHomeBinding
import com.example.cinephile.model.SliderItems
import com.example.cinephile.repository.UserRepository
import com.example.cinephile.repository.UserRepositoryImpl
import com.example.cinephile.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class MainFragment : Fragment() {

//        private var _binding: FragmentHomeBinding? = null
//        private val binding get() = _binding!!

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
