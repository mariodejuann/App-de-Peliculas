package es.usj.alu163300.mdj_moviesapp

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import es.usj.alu163300.mdj_moviesapp.adapters.MovieAdapter
import es.usj.alu163300.mdj_moviesapp.models.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import android.content.Intent
import android.widget.Button
import android.content.Context

class MainActivity : Activity() {
    private lateinit var recyclerMovies: RecyclerView

    companion object {
        const val SERVER = "10.0.2.2"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerMovies = findViewById(R.id.recyclerMovies)

        recyclerMovies.layoutManager = LinearLayoutManager(this)

        val buttonAddMovie = findViewById<Button>(R.id.buttonAddMovie)
        val buttonContact = findViewById<Button>(R.id.buttonContact)

        buttonAddMovie.setOnClickListener {

            val intent = Intent(this, AddMovieActivity::class.java)

            startActivity(intent)
        }

        buttonContact.setOnClickListener {
            val intent = Intent(this, ContactActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        loadMovies()
    }

    private fun loadMovies() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("http://$SERVER:8080/movies")
                val result = url.readText()

                saveMoviesCache(result)

                val movies = Gson().fromJson(result, Array<Movie>::class.java)

                runOnUiThread {
                    recyclerMovies.adapter = MovieAdapter(movies)
                }

            } catch (e: Exception) {
                val cachedJson = loadMoviesCache()

                runOnUiThread {
                    if (cachedJson != null) {
                        val movies = Gson().fromJson(cachedJson, Array<Movie>::class.java)
                        recyclerMovies.adapter = MovieAdapter(movies)
                    }
                }
            }
        }
    }

    private fun saveMoviesCache(json: String) {
        val preferences = getSharedPreferences("movies_cache", Context.MODE_PRIVATE)
        preferences.edit()
            .putString("movies_json", json)
            .apply()
    }

    private fun loadMoviesCache(): String? {
        val preferences = getSharedPreferences("movies_cache", Context.MODE_PRIVATE)
        return preferences.getString("movies_json", null)
    }

}