package com.joseapps.lapetadopt.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.joseapps.lapetadopt.di.AppContainer
import com.joseapps.lapetadopt.ui.browse.BrowseScreen
import com.joseapps.lapetadopt.ui.browse.BrowseViewModel
import com.joseapps.lapetadopt.ui.detail.PetDetailScreen
import com.joseapps.lapetadopt.ui.detail.PetDetailViewModel
import com.joseapps.lapetadopt.ui.favorites.FavoritesScreen
import com.joseapps.lapetadopt.ui.favorites.FavoritesViewModel
import com.joseapps.lapetadopt.util.ViewModelFactory

private data class TopLevelDestination(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val topLevelDestinations = listOf(
    TopLevelDestination(Routes.Browse.route, "Browse", Icons.Filled.Pets),
    TopLevelDestination(Routes.Favorites.route, "Favorites", Icons.Filled.Favorite)
)

@Composable
fun LaPetAdoptNavHost(container: AppContainer) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute == null || topLevelDestinations.any { it.route == currentRoute }) {
                NavigationBar {
                    topLevelDestinations.forEach { destination ->
                        NavigationBarItem(
                            selected = currentRoute == destination.route,
                            onClick = {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(destination.icon, contentDescription = destination.label) },
                            label = { Text(destination.label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Browse.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.Browse.route) {
                val viewModel: BrowseViewModel = viewModel(
                    factory = ViewModelFactory {
                        BrowseViewModel(
                            petRepository = container.petRepository,
                            favoritesRepository = container.favoritesRepository,
                            selectedPetHolder = container.selectedPetHolder
                        )
                    }
                )
                BrowseScreen(viewModel = viewModel) { petId ->
                    navController.navigate(Routes.Detail.createRoute(petId))
                }
            }

            composable(Routes.Favorites.route) {
                val viewModel: FavoritesViewModel = viewModel(
                    factory = ViewModelFactory {
                        FavoritesViewModel(
                            favoritesRepository = container.favoritesRepository,
                            selectedPetHolder = container.selectedPetHolder
                        )
                    }
                )
                FavoritesScreen(viewModel = viewModel) { petId ->
                    navController.navigate(Routes.Detail.createRoute(petId))
                }
            }

            composable(
                route = Routes.Detail.route,
                arguments = listOf(navArgument("petId") { type = NavType.LongType })
            ) { entry ->
                val petId = entry.arguments?.getLong("petId") ?: 0L
                val viewModel: PetDetailViewModel = viewModel(
                    key = "detail_$petId",
                    factory = ViewModelFactory {
                        PetDetailViewModel(
                            petId = petId,
                            petRepository = container.petRepository,
                            favoritesRepository = container.favoritesRepository,
                            selectedPetHolder = container.selectedPetHolder
                        )
                    }
                )
                PetDetailScreen(viewModel = viewModel) { navController.popBackStack() }
            }
        }
    }
}
