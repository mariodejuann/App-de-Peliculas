package es.usj.alu163300.mdj_moviesapp

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import es.usj.alu163300.mdj_moviesapp.models.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import android.app.AlertDialog

class EditMovieActivity : Activity() {

    companion object {
        const val SERVER = "10.0.2.2"
    }

    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_movie)

        val editTitle = findViewById<EditText>(R.id.editTitle)
        val editDirector = findViewById<EditText>(R.id.editDirector)
        val editYear = findViewById<EditText>(R.id.editYear)
        val editDescription = findViewById<EditText>(R.id.editDescription)
        val buttonUpdateMovie = findViewById<Button>(R.id.buttonUpdateMovie)
        val buttonDeleteMovie = findViewById<Button>(R.id.buttonDeleteMovie)

        movie = intent.getSerializableExtra("movie") as Movie

        editTitle.setText(movie.title)
        editDirector.setText(movie.director)
        editYear.setText(movie.year.toString())
        editDescription.setText(movie.description)

        buttonUpdateMovie.setOnClickListener {
            if (
                editTitle.text.toString().isEmpty() ||
                editDirector.text.toString().isEmpty() ||
                editYear.text.toString().isEmpty() ||
                editDescription.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val updatedMovie = Movie(
                id = movie.id,
                title = editTitle.text.toString(),
                genres = movie.genres,
                description = editDescription.text.toString(),
                director = editDirector.text.toString(),
                actors = movie.actors,
                year = editYear.text.toString().toInt(),
                length = movie.length,
                rating = movie.rating,
                votes = movie.votes,
                revenue = movie.revenue
            )

            updateMovie(updatedMovie)
        }

        buttonDeleteMovie.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("Delete movie")
                .setMessage("Are you sure you want to delete this movie?")
                .setPositiveButton("Yes") { _, _ ->
                    deleteMovie(movie.id)
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private fun updateMovie(movie: Movie) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("http://$SERVER:8080/movies")
                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "PUT"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val json = Gson().toJson(movie)
                connection.outputStream.write(json.toByteArray())

                connection.responseCode

                runOnUiThread {
                    Toast.makeText(
                        this@EditMovieActivity,
                        "Movie updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@EditMovieActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun deleteMovie(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("http://$SERVER:8080/movies/$movieId")
                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "DELETE"

                connection.responseCode

                runOnUiThread {
                    Toast.makeText(
                        this@EditMovieActivity,
                        "Movie deleted successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@EditMovieActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

}