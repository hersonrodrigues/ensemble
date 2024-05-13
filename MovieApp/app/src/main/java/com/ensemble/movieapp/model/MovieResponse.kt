package com.ensemble.movieapp.model

data class MovieResponse (
    val success: Boolean,
    val error: String,
    val result: List<Movie>
)