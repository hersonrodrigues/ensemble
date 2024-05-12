package com.ensemble.movieapp.ui.model
data class MovieResponse (
    val success: Boolean,
    val error: String,
    val result: List<Movie>,
)