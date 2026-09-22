package es.usj.alu163300.mdj_moviesapp

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import es.usj.alu163300.mdj_moviesapp.models.Movie
import android.content.Intent
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.net.URL

class ViewMovieActivity : Activity() {

    companion object {
        const val TMDB_API_KEY = "TU_API_KEY_AQUI"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_view_movie)

        val textDetailTitle = findViewById<TextView>(R.id.textDetailTitle)
        val textDetailDirector = findViewById<TextView>(R.id.textDetailDirector)
        val textDetailYear = findViewById<TextView>(R.id.textDetailYear)
        val textDetailRating = findViewById<TextView>(R.id.textDetailRating)
        val textDetailDescription = findViewById<TextView>(R.id.textDetailDescription)
        val buttonActors = findViewById<Button>(R.id.buttonActors)
        val buttonGenres = findViewById<Button>(R.id.buttonGenres)
        val imagePoster = findViewById<ImageView>(R.id.imagePoster)

        val movie = intent.getSerializableExtra("movie") as Movie

        textDetailTitle.text = movie.title
        textDetailDirector.text = "Director: ${movie.director}"
        textDetailYear.text = "Year: ${movie.year}"
        textDetailRating.text = "Rating: ${movie.rating}"
        textDetailDescription.text = movie.description
        loadPoster(movie.title, imagePoster)

        buttonActors.setOnClickListener {

            val intent = Intent(this, ActorsActivity::class.java)

            intent.putIntegerArrayListExtra(
                "actorIds",
                ArrayList(movie.actors)
            )

            startActivity(intent)
        }

        buttonGenres.setOnClickListener {

            val intent = Intent(this, GenresActivity::class.java)

            intent.putIntegerArrayListExtra(
                "genreIds",
                ArrayList(movie.genres)
            )

            startActivity(intent)
        }

    }

    private fun loadPoster(title: String, imagePoster: ImageView) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val encodedTitle = URLEncoder.encode(title, "UTF-8")
                val url = URL(
                    "https://api.themoviedb.org/3/search/movie?api_key=$TMDB_API_KEY&query=$encodedTitle"
                )

                val result = url.readText()
                val jsonObject = JsonParser.parseString(result).asJsonObject
                val results = jsonObject.getAsJsonArray("results")

                if (results.size() > 0) {
                    val firstMovie = results[0].asJsonObject
                    val posterPath = firstMovie.get("poster_path")?.asString

                    if (posterPath != null) {
                        val posterUrl = "https://image.tmdb.org/t/p/w500$posterPath"

                        runOnUiThread {
                            Glide.with(this@ViewMovieActivity)
                                .load(posterUrl)
                                .into(imagePoster)
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
