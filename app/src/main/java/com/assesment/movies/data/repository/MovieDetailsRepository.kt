package com.assesment.movies.data.repository

import com.assesment.movies.data.local.MoviesDao
import com.assesment.movies.data.remote.MovieInterface
import com.assesment.movies.utils.BaseDataSource
import com.assesment.movies.utils.Constants
import com.assesment.movies.utils.performGetOperation

class MovieDetailsRepository(
    private val movieInterface: MovieInterface,
    private val movieDao: MoviesDao
) :
    BaseDataSource() {


    fun getMovieDetails(id: String) = performGetOperation(
        databaseQuery = { movieDao.getSingleMovie(id) },
        networkCall = { getResult { movieInterface.getMovieDetails(id, Constants.API_KEY) } },
        saveCallResult = { movieDao.insertMovieSingle(it) }
    )

}