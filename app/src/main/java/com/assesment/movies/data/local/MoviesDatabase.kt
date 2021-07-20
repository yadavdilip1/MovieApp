package com.assesment.movies.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.assesment.movies.data.entities.Movie
import com.assesment.movies.data.entities.MovieDetails

@Database(entities = [Movie::class, MovieRemoteKey::class, MovieDetails::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {

    companion object {
        fun getInstance(context: Context): MoviesDatabase {
            return Room.databaseBuilder(context, MoviesDatabase::class.java, "name").build()
        }
    }

    abstract fun getMoviesDao(): MoviesDao

}