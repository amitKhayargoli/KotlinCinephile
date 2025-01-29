package com.example.cinephile.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.cinephile.databinding.ViewholderFilmBinding
import com.example.cinephile.model.ShowModel
import com.example.cinephile.ui.activity.FilmDetailActivity

class ShowListAdapter(private val context: Context, private val items: ArrayList<ShowModel>) :
    RecyclerView.Adapter<ShowListAdapter.Viewholder>() {

    inner class Viewholder(private val binding: ViewholderFilmBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(show:ShowModel) {
            binding.nameText.text = show.showName

            val requestOptions = RequestOptions()
                .transform(CenterCrop(), RoundedCorners(30))

            Glide.with(context)
                .load(show.imageUrl)
                .apply(requestOptions)
                .into(binding.moviePic)

            // Handle item click
//            binding.root.setOnClickListener {
//                val intent = Intent(context, FilmDetailActivity::class.java).apply {
//                    putExtra("Show_ID", film.showId)
//                }
//                context.startActivity(intent)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

        val binding = ViewholderFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
