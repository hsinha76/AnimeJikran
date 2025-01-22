package com.hsdroid.animejikran.ui.view.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.hsdroid.animejikran.model.GenreX
import com.hsdroid.animejikran.ui.theme.GrayE9
import com.hsdroid.animejikran.ui.theme.GrayF6
import com.hsdroid.animejikran.ui.viewModel.DetailsViewModel
import com.hsdroid.animejikran.utils.APIState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun DetailsScreen(receivedId: String, detailsViewModel: DetailsViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        detailsViewModel.getAnimeDetails(receivedId)
    }

    val responseState by detailsViewModel.animeDetailsResponse.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        when (val detailsResponse = responseState) {
            APIState.LOADING -> CircularProgressIndicator()
            is APIState.FAILURE -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = detailsResponse.t.message ?: "Something went wrong",
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(onClick = {
                        detailsViewModel.getAnimeDetails(receivedId)
                    }) {
                        Text(text = "Retry")
                    }
                }
            }

            is APIState.SUCCESS -> {
                AnimeDetailsScreen(
                    receivedId = receivedId,
                    title = detailsResponse.data.data?.title ?: "",
                    plot = detailsResponse.data.data?.synopsis ?: "",
                    genres = detailsResponse.data.data?.genres ?: emptyList(),
                    episodes = detailsResponse.data.data?.episodes ?: 0,
                    posterUrl = detailsResponse.data.data?.images?.jpg?.image_url ?: "",
                    trailerUrl = detailsResponse.data.data?.trailer?.url,
                    rating = detailsResponse.data.data?.score ?: 0.0
                )
            }

            else -> Unit
        }
    }
}

@Composable
fun AnimeDetailsScreen(
    receivedId: String,
    title: String,
    plot: String,
    genres: List<GenreX>,
    episodes: Int,
    rating: Double,
    posterUrl: String,
    trailerUrl: String?,
    detailsViewModel: DetailsViewModel = hiltViewModel()
) {

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        detailsViewModel.getAnimeCharacters(receivedId)
    }

    val characterResponseState by detailsViewModel.animeCharactersResponse.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp)
        ) {
            if (trailerUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = posterUrl,
                    contentDescription = "Poster",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.FillBounds
                )
            } else {
                val videoId = trailerUrl.substringAfter("v=")
                YoutubeScreen(videoId = videoId, modifier = Modifier.fillMaxSize())
            }
        }

        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Plot/Synopsis:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = plot, style = MaterialTheme.typography.bodyMedium, color = Color.Black
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Genre(s):",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            items(genres) {
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = GrayE9),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        text = it.name ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                }
            }
        }

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Main Cast:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.TopStart
        ) {
            when (val charactersResponse = characterResponseState) {
                APIState.LOADING -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is APIState.FAILURE -> {
                    Text(
                        text = "No info available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                }

                is APIState.SUCCESS -> {
                    Text(
                        text = charactersResponse.data.data?.name ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                }

                else -> Unit
            }
        }

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Number of Episodes:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = episodes.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )

        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp)) {
            Text(
                text = "Ratings:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .background(
                        GrayF6, shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = rating.toString(), color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
fun YoutubeScreen(
    videoId: String,
    modifier: Modifier
) {
    AndroidView(modifier = modifier, factory = {
        val view = YouTubePlayerView(it)
        view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })
        view
    })
}