package com.hsdroid.animejikran.ui.view.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.hsdroid.animejikran.R
import com.hsdroid.animejikran.ui.theme.GrayE9
import com.hsdroid.animejikran.ui.theme.GrayF6
import com.hsdroid.animejikran.ui.viewModel.HomeViewModel
import com.hsdroid.animejikran.utils.APIState

@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = hiltViewModel()) {

    val scrollState = rememberLazyListState()
    val responseState by homeViewModel.topAnimeResponse.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        when (val response = responseState) {
            APIState.LOADING -> CircularProgressIndicator()
            is APIState.FAILURE -> {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = response.t.message ?: "Something went wrong",
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }

            is APIState.SUCCESS -> {
                LazyColumn(state = scrollState) {
                    items(response.data.data ?: emptyList()) {
                        AnimeCard(id = it.mal_id ?: 0,
                            title = it.title ?: "",
                            episodes = it.episodes ?: 0,
                            rating = it.score ?: 0.0,
                            posterUrl = it.images?.jpg?.image_url ?: "",
                            onClick = { id ->
                                if (id != 0) { //0 represents null value here
                                    navController.navigate("details/$id")
                                }
                            })
                    }
                }
            }

            else -> Unit
        }
    }
}

@Composable
fun AnimeCard(
    id: Int, title: String, episodes: Int, rating: Double, posterUrl: String, onClick: (Int) -> Unit
) {
    Card(modifier = Modifier
        .clickable {
            onClick(id)
        }
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 8.dp)
        .heightIn(min = 150.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = GrayE9)) {

        Box {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = posterUrl,
                    contentDescription = "Poster",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.FillBounds,
                    placeholder = painterResource(R.drawable.placeholder)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterVertically)
                        .weight(1f), verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Episodes: $episodes", color = Color.Black,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(
                        GrayF6, shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp),
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
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}