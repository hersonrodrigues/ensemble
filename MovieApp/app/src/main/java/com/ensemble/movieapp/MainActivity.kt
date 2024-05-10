package com.ensemble.movieapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ensemble.movieapp.ui.theme.EnsembleTheme
import com.ensemble.movie.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnsembleTheme {
                val viewModel : MainViewModel = viewModel()
                val searchText = viewModel.searchText.collectAsState()
                val movies = viewModel.movies.collectAsState()
                val isSearching = viewModel.isSearching.collectAsState()
                val context = LocalContext.current
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
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            items(movies.value) { movie ->
                                Box(
                                    modifier = Modifier
                                        .clickable(
                                            onClick = {},
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = rememberRipple(
                                                color = MaterialTheme.colorScheme.primary
                                            ),
                                        )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        AsyncImage(
                                            model = movie.poster,
                                            contentDescription = "Translated description of what the image contains",
                                            modifier = Modifier.size(120.dp)
                                        )
                                        Column {
                                            Text(
                                                text = movie.title,
                                                style = TextStyle(
                                                    fontSize = 24.sp,
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                modifier = Modifier
                                                    .fillMaxWidth()
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
                                            Button(onClick = {
                                                Toast.makeText(context, "Do nothing with ${movie.title}", Toast.LENGTH_SHORT).show()
                                            }) {
                                                Text(text = "Button")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}