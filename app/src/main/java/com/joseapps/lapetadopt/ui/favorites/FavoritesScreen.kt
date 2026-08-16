package com.joseapps.lapetadopt.ui.favorites

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.joseapps.lapetadopt.ui.components.EmptyState
import com.joseapps.lapetadopt.ui.components.PetCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onPetSelected: (Long) -> Unit
) {
    val favorites by viewModel.favorites.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Favorites") }) }
    ) { padding ->
        if (favorites.isEmpty()) {
            EmptyState(
                message = "No favorites yet. Tap the heart on a pet you like to save it here — it'll work offline too.",
                modifier = Modifier.padding(padding)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                modifier = Modifier.fillMaxSize().padding(padding)
            ) {
                items(favorites, key = { it.id }) { pet ->
                    PetCard(
                        pet = pet,
                        isFavorite = true,
                        onClick = {
                            viewModel.onPetTapped(pet)
                            onPetSelected(pet.id)
                        },
                        onToggleFavorite = { viewModel.removeFavorite(pet.id) },
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
        }
    }
}
