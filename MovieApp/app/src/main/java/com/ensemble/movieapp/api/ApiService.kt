package com.ensemble.movieapp.api

import com.ensemble.movieapp.ui.model.LikeRequest
import com.ensemble.movieapp.ui.model.MovieResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("api/movies")
    suspend fun getMovies(@Query("search") search: String? = null) : Response<MovieResponse>

    @GET("api/movies/like")
    suspend fun getLikedMovies(@Query("deviceId") deviceId: String?)

    @POST("api/movies/like")
    suspend fun likeMovie(@Body likeRequest: LikeRequest?) : Boolean
}