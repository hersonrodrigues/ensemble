package com.ensemble.movieapp.repository

import com.ensemble.movieapp.api.ApiClient
import com.ensemble.movieapp.api.ApiService
import com.ensemble.movieapp.model.LikeRequest
import com.ensemble.movieapp.model.LikeResponse
import com.ensemble.movieapp.model.MovieResponse
import retrofit2.Response

class MovieRepository : ApiService {
    override suspend fun getMovies(search: String?): Response<MovieResponse> {
        return ApiClient.api.getMovies(search)
    }

    override suspend fun getLikedMovies(deviceId: String): Response<LikeResponse> {
        return ApiClient.api.getLikedMovies(deviceId)
    }

    override suspend fun likeMovie(likeRequest: LikeRequest): Boolean {
        return ApiClient.api.likeMovie(likeRequest)
    }
}