package com.news.smartnewsaggregator.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.news.smartnewsaggregator.ui.features.MainScreen
import com.news.smartnewsaggregator.ui.features.detail.DetailScreen
import com.news.smartnewsaggregator.ui.features.favorites.FavoritesScreen

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Favorites : Screen("favorites")
    object Detail : Screen("detail/{articleUrl}") {
        fun createRoute(articleUrl: String) = "detail/$articleUrl"
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route,
        modifier = modifier
    ) {
        composable(Screen.Main.route) {
            MainScreen(navController = navController, snackbarHostState = snackbarHostState)
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(navController = navController)
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("articleUrl") { type = NavType.StringType })
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("articleUrl") ?: ""
            DetailScreen(url = url)
        }
    }
}
