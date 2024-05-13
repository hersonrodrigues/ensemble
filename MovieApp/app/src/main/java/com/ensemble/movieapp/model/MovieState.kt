package com.ensemble.movieapp.model

data class MovieState (
    val success: Boolean,
    val error: String,
    val movies: List<Movie>
)