package com.assesment.movies.ui.movies

import androidx.lifecycle.*
import androidx.paging.*
import com.assesment.movies.data.entities.MovieDetails
import com.assesment.movies.data.local.MoviesDao
import com.assesment.movies.data.paging.MoviesRemoteMediator
import com.assesment.movies.data.remote.MovieInterface
import com.assesment.movies.data.repository.MovieDetailsRepository
import com.assesment.movies.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieInterface: MovieInterface,
    private val repository: MovieDetailsRepository,
    private val movieDao: MoviesDao,
) : ViewModel() {


    @ExperimentalPagingApi
    val list = Pager(
        PagingConfig(pageSize = 10),
        remoteMediator = MoviesRemoteMediator(movieDao, movieInterface, 1)
    ) {
        movieDao.getAllMovies()
    }.liveData.cachedIn(viewModelScope)

    private val _id = MutableLiveData<String>()

    private val _movie = _id.switchMap { id ->
        repository.getMovieDetails(id)
    }
    val movieDetails: LiveData<Result<MovieDetails>> = _movie


    fun getMovieDetails(id: String) {
        _id.value = id
    }


}