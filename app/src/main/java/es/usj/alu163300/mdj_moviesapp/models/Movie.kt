package es.usj.alu163300.mdj_moviesapp.models

import java.io.Serializable

data class Movie(
    val id: Int,
    val title: String,
    val genres: List<Int>,
    val description: String,
    val director: String,
    val actors: List<Int>,
    val year: Int,
    val length: Int,
    val rating: Float,
    val votes: Int,
    val revenue: Float
) : Serializable