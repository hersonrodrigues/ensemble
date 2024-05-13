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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ensemble.movieapp.model.Movie
import com.ensemble.movieapp.ui.theme.EnsembleTheme
import com.ensemble.movieapp.ui.viewmodel.MainViewModel
import kotlin.reflect.KFunction1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnsembleTheme {
                val viewModel: MainViewModel = viewModel()
                val moviesState = viewModel.movieState
                val searchText = viewModel.searchText.collectAsState()
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

                    if (moviesState.success) {
                        MovieComponent(
                            viewModel = viewModel
                        )
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
    viewModel: MainViewModel
) {
    val isSearching = viewModel.isSearching.collectAsState()

    Spacer(modifier = Modifier.height(16.dp))

    if (isSearching.value) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        MovieList(
            viewModel = viewModel,
        )
    }
}

@Composable
fun MovieList(
    viewModel: MainViewModel,
) {
    val movies = viewModel.movieState.movies

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        itemsIndexed(movies) { index, movie ->
            MovieCard(
                viewModel = viewModel,
                movie = movie,
                index = index
            )
        }
    }
}

@Composable
fun MovieCard(
    viewModel: MainViewModel,
    movie: Movie,
    index: Int
) {
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
                                Log.i("MovieClick", "Do nothing on button click!")
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(text = "Button")
                        }
                        IconButton(
                            onClick = {
                                viewModel.handleLikeClick(movie)
                            }, // Trigger like button callback
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            // Use different icons based on like status
                            val icon = if (movie.liked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
                            val color = if (movie.liked) Color.Red else MaterialTheme.colorScheme.primary
                            Icon(icon, contentDescription = "Like", tint = color)
                        }
                    }
                }
            }
        }
    }
}
