package com.example.cinephile.Adapter
import com.bumptech.glide.Glide
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.cinephile.databinding.ViewholderSliderBinding
import com.example.cinephile.model.SliderItems

class SliderAdapter(private var sliderItems: MutableList<SliderItems>
,private val viewPager2:ViewPager2):RecyclerView .Adapter<SliderAdapter.SliderViewHolder>(){

    private var context: Context?=null
    // Handler to manage the automatic scrolling
    private val handler = Handler(Looper.getMainLooper())

    inner class SliderViewHolder(private val binding:ViewholderSliderBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(sliderItem: SliderItems){
            context?.let{
                Glide.with(it).load(sliderItem.image)
                    .apply{RequestOptions().transform(CenterCrop(), RoundedCorners(60))}
                    .into(binding.imageSlide)
        }

    }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        context=parent.context
        val binding = ViewholderSliderBinding.inflate(LayoutInflater.from(context),parent,false)
        return SliderViewHolder(binding)
    }

    override fun getItemCount(): Int=sliderItems.size

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(sliderItems[position])
//        if(position==sliderItems.size-2 ){
//            viewPager2.post(runnable)
//            handler.removeCallbacks(runnable)
//            handler.postDelayed(runnable,3000)
//    }

}
}