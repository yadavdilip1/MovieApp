package com.assesment.movies.data.local

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.assesment.movies.data.entities.Movie
import com.assesment.movies.data.entities.MovieDetails

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(list: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieSingle(movie: MovieDetails)

    @Query("SELECT * FROM Movie")
    fun getAllMovies(): PagingSource<Int, Movie>

    @Query("SELECT * FROM MovieDetails WHERE imdbID = :id")
    fun getSingleMovie(id: String): LiveData<MovieDetails>


    @Query("DELETE FROM Movie")
    suspend fun deleteAllMovies()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRemoteKeys(list: List<MovieRemoteKey>)


    @Query("SELECT * FROM MovieRemoteKey WHERE id = :id")
    suspend fun getAllREmoteKey(id: String): MovieRemoteKey?

    @Query("DELETE FROM MovieRemoteKey")
    suspend fun deleteAllRemoteKeys()

}