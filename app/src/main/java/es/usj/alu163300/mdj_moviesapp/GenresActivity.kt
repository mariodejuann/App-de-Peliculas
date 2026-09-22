package es.usj.alu163300.mdj_moviesapp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import com.google.gson.Gson
import es.usj.alu163300.mdj_moviesapp.models.Genre
import es.usj.alu163300.mdj_moviesapp.models.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class GenresActivity : Activity() {

    companion object {
        const val SERVER = "10.0.2.2"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genres)

        val textGenres = findViewById<TextView>(R.id.textGenres)
        val genreIds = intent.getIntegerArrayListExtra("genreIds") ?: arrayListOf()

        loadGenresAndMovies(genreIds, textGenres)
    }

    private fun loadGenresAndMovies(genreIds: ArrayList<Int>, textGenres: TextView) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val genresJson = URL("http://$SERVER:8080/genres").readText()
                val moviesJson = URL("http://$SERVER:8080/movies").readText()

                saveGenresCache(genresJson)

                val genres = Gson().fromJson(genresJson, Array<Genre>::class.java)
                val movies = Gson().fromJson(moviesJson, Array<Movie>::class.java)

                val text = buildGenresText(genreIds, genres, movies)

                runOnUiThread {
                    textGenres.text = text
                }

            } catch (e: Exception) {

                val cachedGenres = loadGenresCache()
                val cachedMovies = loadMoviesCache()

                runOnUiThread {
                    if (cachedGenres != null && cachedMovies != null) {
                        val genres = Gson().fromJson(cachedGenres, Array<Genre>::class.java)
                        val movies = Gson().fromJson(cachedMovies, Array<Movie>::class.java)

                        textGenres.text = buildGenresText(genreIds, genres, movies)
                    } else {
                        textGenres.text = "Error: ${e.message}"
                    }
                }
            }
        }
    }

    private fun buildGenresText(
        genreIds: ArrayList<Int>,
        genres: Array<Genre>,
        movies: Array<Movie>
    ): String {

        val text = StringBuilder()

        for (genre in genres) {
            if (genreIds.contains(genre.id)) {
                text.append(genre.name)
                text.append("\n")

                for (movie in movies) {
                    if (movie.genres.contains(genre.id)) {
                        text.append(" - ")
                        text.append(movie.title)
                        text.append("\n")
                    }
                }

                text.append("\n")
            }
        }

        return text.toString()
    }

    private fun saveGenresCache(json: String) {
        val preferences = getSharedPreferences("genres_cache", Context.MODE_PRIVATE)
        preferences.edit()
            .putString("genres_json", json)
            .apply()
    }

    private fun loadGenresCache(): String? {
        val preferences = getSharedPreferences("genres_cache", Context.MODE_PRIVATE)
        return preferences.getString("genres_json", null)
    }

    private fun loadMoviesCache(): String? {
        val preferences = getSharedPreferences("movies_cache", Context.MODE_PRIVATE)
        return preferences.getString("movies_json", null)
    }
}