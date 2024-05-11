package com.ensemble.movieapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ensemble.movie.model.Movie
import com.ensemble.movieapp.ui.theme.EnsembleTheme
import com.ensemble.movieapp.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnsembleTheme {
                val viewModel: MainViewModel = viewModel()
                val searchText = viewModel.searchText.collectAsState()
                val movies = viewModel.movies.collectAsState()
                val isSearching = viewModel.isSearching.collectAsState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    TextField(
                        value = searchText.value,
                        onValueChange = viewModel::onSearch,
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
                            movies = movies.value,
                            onItemClick = {
                                Log.i("MovieCardButton", "Do nothing when click on the button. Movie: ${it.title}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MovieList(
    movies: List<Movie>,
    onItemClick: (Movie) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        itemsIndexed(movies) { index, movie ->
            MovieCard(movie = movie, onItemClick = onItemClick, index = index)
        }
    }
}

@Composable
fun MovieCard(
    movie: Movie,
    onItemClick: (Movie) -> Unit,
    index: Int
) {
    // Define different background color for odd/even elements
    val backgroundColor = if (index % 2 == 0) {
        MaterialTheme.colorScheme.background
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    Surface(
        color = backgroundColor,
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = movie.poster,
                contentDescription = "Image cover of the movie: ${movie.title}",
                modifier = Modifier.size(120.dp)
            )
            Column {
                Text(
                    text = movie.title,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = movie.year,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                )
                Button(
                    onClick = {
                        onItemClick(movie)
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(text = "Button")
                }
            }
        }
    }
}