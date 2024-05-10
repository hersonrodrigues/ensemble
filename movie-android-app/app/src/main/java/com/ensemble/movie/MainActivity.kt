package com.ensemble.movie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ensemble.movie.ui.theme.EnsembleTheme
import com.ensemble.movie.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnsembleTheme {
                val viewModel = viewModel<MainViewModel>()
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
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            cursorColor = Color.Black,
                            disabledLabelColor = Color.Gray,
                            focusedIndicatorColor = MaterialTheme.colors.primary,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
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
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            items(movies.value) { movie ->
                                Column {
                                    AsyncImage(
                                        model = "https://example.com/image.jpg",
                                        contentDescription = "Translated description of what the image contains"
                                    )
                                    Text(
                                        text = "${movie.title} (${movie.year})",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 16.dp)
                                    )
                                }
                                
                            }
                        }
                    }
                }
            }
        }
    }
}