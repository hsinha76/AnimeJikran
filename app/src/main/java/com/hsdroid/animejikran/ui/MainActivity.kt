package com.hsdroid.animejikran.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hsdroid.animejikran.ui.theme.AnimeJikranTheme
import com.hsdroid.animejikran.ui.view.details.DetailsScreen
import com.hsdroid.animejikran.ui.view.home.HomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimeJikranTheme {
                NavGraph()
            }
        }
    }
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }

        composable("details/{id}", arguments = listOf(navArgument("id") {
            type = NavType.StringType
        })) {
            val receivedId = it.arguments?.getString("id")
            if (!receivedId.isNullOrEmpty()) {
                DetailsScreen(receivedId)
            }
        }
    }
}