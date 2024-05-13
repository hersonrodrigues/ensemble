package com.ensemble.movieapp.model
data class LikeRequest(
    val deviceId: String,
    val imdbID: String,
    val flag: Boolean,
)