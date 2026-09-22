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

class AddMovieActivity : Activity() {

    companion object {
        const val SERVER = "10.0.2.2"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)

        val editTitle = findViewById<EditText>(R.id.editTitle)
        val editDirector = findViewById<EditText>(R.id.editDirector)
        val editYear = findViewById<EditText>(R.id.editYear)
        val editDescription = findViewById<EditText>(R.id.editDescription)
        val buttonSaveMovie = findViewById<Button>(R.id.buttonSaveMovie)

        buttonSaveMovie.setOnClickListener {
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

            val movie = Movie(
                id = 0,
                title = editTitle.text.toString(),
                genres = emptyList(),
                description = editDescription.text.toString(),
                director = editDirector.text.toString(),
                actors = emptyList(),
                year = editYear.text.toString().toInt(),
                length = 120,
                rating = 0.0f,
                votes = 0,
                revenue = 0.0f
            )

            saveMovie(movie)
        }
    }

    private fun saveMovie(movie: Movie) {

        CoroutineScope(Dispatchers.IO).launch {

            try {

                val url = URL("http://$SERVER:8080/movies")
                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val json = Gson().toJson(movie)

                connection.outputStream.write(json.toByteArray())

                connection.responseCode

                runOnUiThread {
                    Toast.makeText(
                        this@AddMovieActivity,
                        "Movie saved successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                }

            } catch (e: Exception) {

                runOnUiThread {
                    Toast.makeText(
                        this@AddMovieActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}