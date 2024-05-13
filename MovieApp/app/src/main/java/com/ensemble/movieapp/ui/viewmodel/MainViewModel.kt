package com.ensemble.movieapp.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ensemble.movieapp.repository.MovieRepository
import com.ensemble.movieapp.model.LikeRequest
import com.ensemble.movieapp.model.Movie
import com.ensemble.movieapp.model.MovieState
import com.ensemble.movieapp.utils.AndroidId
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>()

    private val deviceId by lazy {
        AndroidId().getDeviceId(context)
    }

    private var likedMovieList = mutableListOf<String>()
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    var movieState by mutableStateOf(
        MovieState(
            success = true,
            error = "",
            movies = emptyList()
        )
    )

    private val movieRepository = MovieRepository()

    init {
        listAllMoviesLiked()

        // Listen for changes to searchText and call handleSearch when it changes
        viewModelScope.launch {
            _searchText.debounce(600).collect { _ ->
                handleSearch()
            }
        }
    }

    private fun listAllMoviesLiked() {
        viewModelScope.launch {
            val response = movieRepository.getLikedMovies(deviceId)
            val moviesId = response.body()?.result

            likedMovieList.clear()
            moviesId?.toCollection(likedMovieList)

            // Update the movieState with liked for each movie
            applyLikesToMovies()
        }
    }

    private fun applyLikesToMovies() {
        // Update the movieState with liked for each movie
        movieState = movieState.copy(
            movies = movieState.movies.map { movie ->
                // Update like attribute based on whether the movie id is present in moviesId list
                movie.copy(liked = likedMovieList.contains(movie.imbId))
            }
        )
    }

    private fun handleSearch() {
        viewModelScope.launch {
            _isSearching.value = true
            val response = movieRepository.getMovies(searchText.value)
            movieState = movieState.copy(
                success = response.body()?.error.isNullOrEmpty(),
                movies = response.body()?.result ?: emptyList(),
                error = response.body()?.error ?: ""
            )
            _isSearching.value = false
            applyLikesToMovies()
        }
    }

    fun handleLikeClick(movie: Movie) {
        viewModelScope.launch {
            val request = LikeRequest(
                deviceId = deviceId,
                imdbID = movie.imbId,
                flag = !movie.liked
            )
            try {
                // Register the movie like on the API
                movieRepository.likeMovie(request)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if(!movie.liked) {
            likedMovieList.add(movie.imbId)
        } else {
            likedMovieList.remove(movie.imbId)
        }

        applyLikesToMovies()
    }

    fun onSearch(text: String) {
        _searchText.value = text
    }
}