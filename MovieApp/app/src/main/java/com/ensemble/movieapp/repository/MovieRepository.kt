package com.ensemble.movieapp.repository

import com.ensemble.movieapp.api.ApiClient
import com.ensemble.movieapp.ui.model.MovieResponse
import retrofit2.Response

class MovieRepository {
    suspend fun getMovie(search: String? = null): Response<MovieResponse> {
        return ApiClient.api.getMovies(search)
    }
}