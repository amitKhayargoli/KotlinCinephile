import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinephile.R
import com.example.cinephile.model.MovieModel

class SearchAdapter(private val context: Context, private val movieList: List<MovieModel>) :
    RecyclerView.Adapter<SearchAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.viewholder_film, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.nameText.text = movie.movieName
        Glide.with(context)
            .load(movie.imageUrl)
            .into(holder.moviePic)
    }

    override fun getItemCount(): Int = movieList.size

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val moviePic: ImageView = itemView.findViewById(R.id.moviePic)
        val nameText: TextView = itemView.findViewById(R.id.nameText)
    }
}
