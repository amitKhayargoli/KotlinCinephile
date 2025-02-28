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
import com.example.cinephile.ui.activity.FilmDetailActivity

class MovieListAdapter(private val context: Context, private val items: ArrayList<MovieModel>) :
    RecyclerView.Adapter<MovieListAdapter.Viewholder>() {

    inner class Viewholder(private val binding: ViewholderFilmBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(film: MovieModel) {
            binding.nameText.text = film.movieName

            val requestOptions = RequestOptions()
                .transform(CenterCrop(), RoundedCorners(30))

            Glide.with(context)
                .load(film.imageUrl)
                .apply(requestOptions)
                .into(binding.moviePic)

            binding.root.setOnClickListener {
                val intent = Intent(context, FilmDetailActivity::class.java).apply {
                    putExtra("movieID", film.movieId)
                    putExtra("movieTitle", film.movieName)
                    putExtra("movieDesc", film.movieSummary)
                    putExtra("movieYear", film.movieYear)
                    putExtra("movieImdb", film.IMDB)
                    putExtra("filmPic", film.imageUrl)
                    putExtra("movieRuntime",film.movieRuntime)
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

    fun updateData(products: List<MovieModel>){
        items.clear()
        items.addAll(products)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size
}
