package com.assesment.movies.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.assesment.movies.data.entities.Movie
import com.assesment.movies.data.local.MovieRemoteKey
import com.assesment.movies.data.local.MoviesDao
import com.assesment.movies.data.remote.MovieInterface
import com.assesment.movies.utils.Constants
import java.io.InvalidObjectException

@ExperimentalPagingApi
class MoviesRemoteMediator(
    private val moviesDao: MoviesDao,
    private val moviesInterface: MovieInterface,
    private val initialPage: Int = 1
) : RemoteMediator<Int, Movie>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): MediatorResult {

        return try {

            // Judging the page number
            val page = when (loadType) {
                LoadType.APPEND -> {

                    val remoteKey =
                        getLastRemoteKey(state) ?: throw InvalidObjectException("Inafjklg")
                    remoteKey.next ?: return MediatorResult.Success(endOfPaginationReached = true)

                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.REFRESH -> {
                    val remoteKey = getClosestRemoteKeys(state)
                    remoteKey?.next?.minus(1) ?: initialPage
                }
            }

            // make network request
            val response = moviesInterface.getAllMovies("Batman", page, Constants.API_KEY)

            val endOfPagination = response.body()?.Search?.size!! < state.config.pageSize

            if (response.isSuccessful) {

                response.body()?.let {

                    // flush our data
                    if (loadType == LoadType.REFRESH) {
                        moviesDao.deleteAllMovies()
                        moviesDao.deleteAllRemoteKeys()
                    }


                    val prev = if (page == initialPage) null else page - 1
                    val next = if (endOfPagination) null else page + 1


                    val list = response.body()?.Search?.map { movie ->
                        MovieRemoteKey(movie.imdbID, prev, next)
                    }


                    // make list of remote keys

                    if (list != null) {
                        moviesDao.insertAllRemoteKeys(list)
                    }

                    // insert to the room
                    moviesDao.insertMovies(it.Search)


                }
                MediatorResult.Success(endOfPagination)
            } else {
                MediatorResult.Success(endOfPaginationReached = true)
            }


        } catch (e: Exception) {
            MediatorResult.Error(e)
        }

    }

    private suspend fun getClosestRemoteKeys(state: PagingState<Int, Movie>): MovieRemoteKey? {

        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.let { movie ->
                moviesDao.getAllREmoteKey(movie.imdbID)
            }
        }

    }


    private suspend fun getLastRemoteKey(state: PagingState<Int, Movie>): MovieRemoteKey? {
        return state.lastItemOrNull()?.let {
            moviesDao.getAllREmoteKey(it.imdbID)
        }
    }

}