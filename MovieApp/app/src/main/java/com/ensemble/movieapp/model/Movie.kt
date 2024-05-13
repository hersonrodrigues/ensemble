package com.ensemble.movieapp.model

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("imdbID") val imbId: String,
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("Poster") val poster: String,
    val liked: Boolean = false,
)
