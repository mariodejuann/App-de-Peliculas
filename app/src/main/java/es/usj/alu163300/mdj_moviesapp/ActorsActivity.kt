package es.usj.alu163300.mdj_moviesapp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import com.google.gson.Gson
import es.usj.alu163300.mdj_moviesapp.models.Actor
import es.usj.alu163300.mdj_moviesapp.models.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class ActorsActivity : Activity() {

    companion object {
        const val SERVER = "10.0.2.2"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actors)

        val textActors = findViewById<TextView>(R.id.textActors)
        val actorIds = intent.getIntegerArrayListExtra("actorIds") ?: arrayListOf()

        loadActorsAndMovies(actorIds, textActors)
    }

    private fun loadActorsAndMovies(actorIds: ArrayList<Int>, textActors: TextView) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val actorsJson = URL("http://$SERVER:8080/actors").readText()
                val moviesJson = URL("http://$SERVER:8080/movies").readText()

                saveActorsCache(actorsJson)

                val actors = Gson().fromJson(actorsJson, Array<Actor>::class.java)
                val movies = Gson().fromJson(moviesJson, Array<Movie>::class.java)

                val text = buildActorsText(actorIds, actors, movies)

                runOnUiThread {
                    textActors.text = text
                }

            } catch (e: Exception) {

                val cachedActors = loadActorsCache()
                val cachedMovies = loadMoviesCache()

                runOnUiThread {
                    if (cachedActors != null && cachedMovies != null) {
                        val actors = Gson().fromJson(cachedActors, Array<Actor>::class.java)
                        val movies = Gson().fromJson(cachedMovies, Array<Movie>::class.java)

                        textActors.text = buildActorsText(actorIds, actors, movies)
                    } else {
                        textActors.text = "Error: ${e.message}"
                    }
                }
            }
        }
    }

    private fun buildActorsText(
        actorIds: ArrayList<Int>,
        actors: Array<Actor>,
        movies: Array<Movie>
    ): String {

        val text = StringBuilder()

        for (actor in actors) {
            if (actorIds.contains(actor.id)) {
                text.append(actor.name)
                text.append("\n")

                for (movie in movies) {
                    if (movie.actors.contains(actor.id)) {
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

    private fun saveActorsCache(json: String) {
        val preferences = getSharedPreferences("actors_cache", Context.MODE_PRIVATE)
        preferences.edit()
            .putString("actors_json", json)
            .apply()
    }

    private fun loadActorsCache(): String? {
        val preferences = getSharedPreferences("actors_cache", Context.MODE_PRIVATE)
        return preferences.getString("actors_json", null)
    }

    private fun loadMoviesCache(): String? {
        val preferences = getSharedPreferences("movies_cache", Context.MODE_PRIVATE)
        return preferences.getString("movies_json", null)
    }
}