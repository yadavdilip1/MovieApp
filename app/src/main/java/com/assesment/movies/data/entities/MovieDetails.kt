package com.assesment.movies.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MovieDetails")
data class MovieDetails(
    val Actors: String?,
    val Awards: String?,
    val Country: String?,
    val DVD: String?,
    val Director: String?,
    val Genre: String?,
    val Language: String?,
    val Metascore: String?,
    val Plot: String?,
    val Poster: String?,
    val Production: String?,
    val Rated: String?,
    val Released: String?,
    val Response: String?,
    val Runtime: String?,
    val Title: String?,
    val Type: String?,
    val Website: String?,
    val Writer: String?,
    val Year: String?,
    @PrimaryKey(autoGenerate = false)
    val imdbID: String,
    val imdbRating: String?,
    val imdbVotes: String?,
)