package es.usj.alu163300.mdj_moviesapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.usj.alu163300.mdj_moviesapp.R
import es.usj.alu163300.mdj_moviesapp.ViewMovieActivity
import es.usj.alu163300.mdj_moviesapp.models.Movie
import es.usj.alu163300.mdj_moviesapp.EditMovieActivity

class MovieAdapter(
    private val movies: Array<Movie>
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textTitle: TextView = view.findViewById(R.id.textTitle)
        val textDirector: TextView = view.findViewById(R.id.textDirector)
        val textYear: TextView = view.findViewById(R.id.textYear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)

        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        val movie = movies[position]

        holder.textTitle.text = movie.title
        holder.textDirector.text = movie.director
        holder.textYear.text = movie.year.toString()


        holder.itemView.setOnClickListener {

            val intent = Intent(holder.itemView.context, ViewMovieActivity::class.java)

            intent.putExtra("movie", movie)

            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {

            val intent = Intent(holder.itemView.context, EditMovieActivity::class.java)

            intent.putExtra("movie", movie)

            holder.itemView.context.startActivity(intent)

            true
        }

    }

    override fun getItemCount(): Int {
        return movies.size
    }
}