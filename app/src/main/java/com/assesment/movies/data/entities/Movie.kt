package com.assesment.movies.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Movie")
data class Movie(
    val Poster: String?,
    val Title: String?,
    val Type: String?,
    val Year: String?,
    @PrimaryKey(autoGenerate = false)
    val imdbID: String
)