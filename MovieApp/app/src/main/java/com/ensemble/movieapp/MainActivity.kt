package com.ensemble.movieapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ensemble.movieapp.ui.model.Movie
import com.ensemble.movieapp.ui.theme.EnsembleTheme
import com.ensemble.movieapp.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnsembleTheme {
                val viewModel: MainViewModel = viewModel()
                val searchText = viewModel.searchText.collectAsState()
                val moviesState = viewModel.movieState
                val isSearching = viewModel.isSearching.collectAsState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    if (moviesState.success) {
                        MovieComponent(searchText, isSearching, viewModel::onSearch, moviesState.result)
                    } else {
                        Box {
                            Text(text = moviesState.error, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieComponent(
    searchText: State<String>,
    isSearching: State<Boolean>,
    onSearch: (String) -> Unit,
    movies: List<Movie>
) {
    TextField(
        value = searchText.value,
        onValueChange = onSearch,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = "Search") },
        placeholder = { Text(text = "Search movies, series...") }
    )
    Spacer(modifier = Modifier.height(16.dp))
    if (isSearching.value) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        MovieList(
            movies = movies,
            onItemClick = {
                Log.i("MovieCardButton", "Do nothing when click on the button. Movie: ${it.title}")
            },
            onLikeClick = {
                Log.i("MovieCardButton", "Liked Movie: ${it.title}")
            }
        )
    }
}

@Composable
fun MovieList(
    movies: List<Movie>,
    onItemClick: (Movie) -> Unit,
    onLikeClick: (Movie) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        itemsIndexed(movies) { index, movie ->
            MovieCard(
                movie = movie,
                onItemClick = onItemClick,
                onLikeClick = onLikeClick,
                index = index,
                likedMovies = arrayListOf("tt2705436")
            )
        }
    }
}

@Composable
fun MovieCard(
    movie: Movie,
    onItemClick: (Movie) -> Unit,
    onLikeClick: (Movie) -> Unit, // Callback for like button click
    index: Int,
    likedMovies: List<String>
) {
    val isLiked by remember { mutableStateOf(likedMovies.contains(movie.imbId)) }

    // Define different background color for odd/even elements
    val backgroundColor = if (index % 2 == 0) {
        MaterialTheme.colorScheme.background
    } else {
        MaterialTheme.colorScheme.surface
    }
    Surface(
        color = backgroundColor,
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart)
            ) {
                AsyncImage(
                    model = movie.poster,
                    contentDescription = "Image cover of the movie: ${movie.title}",
                    modifier = Modifier.size(120.dp)
                )
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(
                        text = movie.title,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = movie.year,
                        style = TextStyle(
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                    )
                    Row {
                        Button(
                            onClick = {
                                onItemClick(movie)
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(text = "Button")
                        }
                        IconButton(
                            onClick = {
                                onLikeClick(movie)
                            }, // Trigger like button callback
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            // Use different icons based on like status
                            val icon = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
                            Icon(icon, contentDescription = "Like")
                        }
                    }
                }
            }
        }
    }
}
