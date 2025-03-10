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
import com.example.cinephile.model.MovieModel
import com.example.cinephile.model.ShowModel
import com.example.cinephile.ui.activity.FilmDetailActivity
import com.example.cinephile.ui.activity.ShowDetailActivity

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
            binding.root.setOnClickListener {
                val intent = Intent(context, ShowDetailActivity::class.java).apply {
                    putExtra("showID", show.showId)
                    putExtra("showName", show.showName)
                    putExtra("showSummary", show.showSummary)
                    putExtra("showYear", show.showYear)
                    putExtra("showImdb", show.IMDB)
                    putExtra("showPic", show.imageUrl)
                    putExtra("showEpisodes",show.showEpisodes)
                    putExtra("showSeasons",show.showSeasons)


                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

        val binding = ViewholderFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bind(items[position])
    }


    fun updateData(movies: List<ShowModel>){
        items.clear()
        items.addAll(movies)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size


}
