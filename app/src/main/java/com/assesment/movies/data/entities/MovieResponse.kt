package com.assesment.movies.data.entities

data class MovieResponse(
    val Response: String,
    val Search: List<Movie>,
    val totalResults: String
)