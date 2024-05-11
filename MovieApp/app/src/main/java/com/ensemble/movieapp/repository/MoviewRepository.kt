package com.ensemble.movieapp.repository

import com.ensemble.movie.model.Movie
import com.ensemble.movieapp.api.ApiService

class MovieRepository(private val apiService: ApiService) {
    suspend fun getMovies(): List<Movie> {
        return try {
            apiService.getMovies()
        } catch (e: Exception) {
            // Handle error
            emptyList()
        }
    }

    suspend fun likeMovie(deviceId: String, movieId: Int): Boolean {
        return try {
            apiService.likeMovie(deviceId, movieId)
        } catch (e: Exception) {
            // Handle error
            false
        }
    }
}