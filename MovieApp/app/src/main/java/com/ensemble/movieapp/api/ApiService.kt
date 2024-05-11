package com.ensemble.movieapp.api

import com.ensemble.movie.model.Movie
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("movies")
    suspend fun getMovies(): List<Movie>

    @POST("like")
    suspend fun likeMovie(
        @Query("deviceId") deviceId: String,
        @Query("movieId") movieId: Int
    ): Boolean
}

