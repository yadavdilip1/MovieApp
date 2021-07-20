package com.assesment.movies.di

import android.content.Context
import com.assesment.movies.BuildConfig
import com.assesment.movies.data.local.MoviesDao
import com.assesment.movies.data.local.MoviesDatabase
import com.assesment.movies.data.remote.MovieInterface
import com.assesment.movies.data.repository.MovieDetailsRepository
import com.assesment.movies.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object HiltModules {

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .build()


    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Singleton
    @Provides
    fun provideRetrofitInterface(retrofit: Retrofit): MovieInterface {
        return retrofit.create(MovieInterface::class.java)
    }


    @Provides
    fun provideRepository(
        movieInterface: MovieInterface,
        movieDao: MoviesDao
    ): MovieDetailsRepository {
        return MovieDetailsRepository(movieInterface, movieDao)
    }

    @Singleton
    @Provides
    fun provideMoviesDatabase(@ApplicationContext context: Context): MoviesDatabase {
        return MoviesDatabase.getInstance(context)
    }


    @Singleton
    @Provides
    fun provideMoviesDao(moviesDatabase: MoviesDatabase): MoviesDao {
        return moviesDatabase.getMoviesDao()
    }


}