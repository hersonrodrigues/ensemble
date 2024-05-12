package com.ensemble.movieapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ensemble.movieapp.repository.MovieRepository
import com.ensemble.movieapp.ui.model.MovieResponse
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    var movieState by mutableStateOf(MovieResponse(true, "", emptyList()))

    private val movieRepository = MovieRepository()

    init {
        // Listen for changes to searchText and call handleSearch when it changes
        viewModelScope.launch {
            _searchText.collect { _ ->
                handleSearch()
            }
        }
    }
    private fun handleSearch() {
        viewModelScope.launch {
            _isSearching.value = true
            val response = movieRepository.getMovie(searchText.value)
            movieState = movieState.copy(
                result = response.body()?.result ?: emptyList()
            )
            _isSearching.value = false
        }
    }

    fun onSearch(text: String) {
        _searchText.value = text
    }
}