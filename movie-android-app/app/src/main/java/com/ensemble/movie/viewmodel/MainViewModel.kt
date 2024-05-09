package com.ensemble.movie.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ensemble.movie.model.Movie
import kotlinx.coroutines.flow.*

class MainViewModel : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _movies = MutableStateFlow(allMovies)
    val movies = searchText
        .debounce(1000L)
        .onEach { _isSearching.update { true } }
        .combine(_movies) { text, movies ->
            if (text.isBlank()) {
                allMovies
            } else {
                movies.filter { it.title.contains(text, ignoreCase = true) }
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _movies.value
        )

    fun onSearch(text: String) {
        _searchText.value = text
    }
}

private val allMovies = listOf<Movie>(
    Movie("Game Of Thrones", "2010", "https://m.media-amazon.com/images/M/MV5BN2IzYzBiOTQtNGZmMi00NDI5LTgxMzMtN2EzZjA1NjhlOGMxXkEyXkFqcGdeQXVyNjAwNDUxODI@._V1_SX300.jpg"),
    Movie("Monsters, Inc.", "2001", "https://m.media-amazon.com/images/M/MV5BMTY1NTI0ODUyOF5BMl5BanBnXkFtZTgwNTEyNjQ0MDE@._V1_SX300.jpg"),
    Movie("I, Robot", "2004", "https://m.media-amazon.com/images/M/MV5BNmE1OWI2ZGItMDUyOS00MmU5LWE0MzUtYTQ0YzA1YTE5MGYxXkEyXkFqcGdeQXVyMDM5ODIyNw@@._V1_SX300.jpg")
)