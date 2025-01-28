package com.example.cinephile

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.cinephile.Adapter.SliderAdapter
import com.example.cinephile.databinding.ActivityMainBinding
import com.example.cinephile.model.SliderItems
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var database: FirebaseDatabase

    val sliderHandler = Handler()

    val sliderRunnable = Runnable{
        binding.viewPager2.currentItem = binding.viewPager2.currentItem+1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        initBanner()
    }

    private fun initBanner() {
        binding.progressBarSlider.visibility= View.VISIBLE

        val lists = mutableListOf<SliderItems>()
        lists.add(SliderItems(image = R.drawable.cap));
        lists.add(SliderItems(image = R.drawable.dune));
        lists.add(SliderItems(image = R.drawable.darkknight));
        lists.add(SliderItems(image = R.drawable.fightclub));
        lists.add(SliderItems(image = R.drawable.johnwick));
        lists.add(SliderItems(image = R.drawable.wide));

        binding.progressBarSlider.visibility = View.GONE
        banners(lists)


    }

    private fun banners(lists: MutableList<SliderItems>) {
        binding.viewPager2.adapter = SliderAdapter(lists,binding.viewPager2)
        binding.viewPager2.clipToPadding = false
        binding.viewPager2.clipChildren = false
        binding.viewPager2.offscreenPageLimit = 3
        binding.viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER


        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
            addTransformer(ViewPager2.PageTransformer{
                page, position ->
                val r = 1-Math.abs(position)
                page.scaleY = 0.85f+r*0.15f
            })
        }

        binding.viewPager2.setPageTransformer(compositePageTransformer)
        binding.viewPager2.currentItem = 1
        binding.viewPager2.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
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

        sliderHandler.postDelayed(sliderRunnable,2000)
    }
}