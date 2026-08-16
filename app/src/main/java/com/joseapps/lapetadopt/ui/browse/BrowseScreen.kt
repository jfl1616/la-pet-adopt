package com.joseapps.lapetadopt.ui.browse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.joseapps.lapetadopt.data.model.PetType
import com.joseapps.lapetadopt.ui.components.EmptyState
import com.joseapps.lapetadopt.ui.components.FullScreenError
import com.joseapps.lapetadopt.ui.components.FullScreenLoading
import com.joseapps.lapetadopt.ui.components.PetCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(
    viewModel: BrowseViewModel,
    onPetSelected: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val urgentPets by viewModel.urgentPets.collectAsState()

    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState: SheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    // Local buffer for the search field so typing feels instant; the actual filter/search only
    // fires 300ms after the user stops typing (debounced below), rather than on every keystroke.
    var searchText by remember(filter.breedQuery) { mutableStateOf(filter.breedQuery) }
    LaunchedEffect(searchText) {
        if (searchText != filter.breedQuery) {
            delay(300)
            viewModel.updateFilter(filter.copy(breedQuery = searchText))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adopt in Los Angeles") },
                actions = {
                    IconButton(onClick = { showFilterSheet = true }) {
                        Icon(Icons.Filled.FilterList, contentDescription = "More filters")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                placeholder = { Text("Search by name or breed") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        IconButton(onClick = { searchText = "" }) {
                            Icon(Icons.Filled.Close, contentDescription = "Clear search")
                        }
                    }
                },
                singleLine = true
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                item {
                    FilterChip(
                        selected = filter.urgentOnly,
                        onClick = { viewModel.updateFilter(filter.copy(urgentOnly = !filter.urgentOnly)) },
                        label = { Text("🚨 Urgent") }
                    )
                }
                item {
                    FilterChip(
                        selected = filter.type == null,
                        onClick = { viewModel.updateFilter(filter.copy(type = null)) },
                        label = { Text("🐾 All") }
                    )
                }
                items(PetType.entries.toTypedArray()) { option ->
                    FilterChip(
                        selected = filter.type == option,
                        onClick = {
                            viewModel.updateFilter(filter.copy(type = if (filter.type == option) null else option))
                        },
                        label = { Text("${option.emoji} ${option.label}") }
                    )
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                when (val state = uiState) {
                    is BrowseUiState.Loading -> FullScreenLoading()
                    is BrowseUiState.Error -> FullScreenError(
                        message = state.message,
                        onRetry = viewModel::retry
                    )
                    is BrowseUiState.Success -> {
                        if (state.pets.isEmpty()) {
                            EmptyState(
                                message = "No adoptable pets matched those filters. Try widening your search."
                            )
                        } else {
                            val gridState = rememberLazyGridState()
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                state = gridState,
                                contentPadding = PaddingValues(12.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                if (urgentPets.isNotEmpty()) {
                                    item(span = { GridItemSpan(2) }) {
                                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Text(
                                                text = "🚨 Needs a home urgently",
                                                style = MaterialTheme.typography.titleMedium,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                            )
                                            LazyRow(
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                contentPadding = PaddingValues(horizontal = 6.dp)
                                            ) {
                                                items(urgentPets, key = { "urgent-${it.id}" }) { pet ->
                                                    PetCard(
                                                        pet = pet,
                                                        isFavorite = favoriteIds.contains(pet.id),
                                                        onClick = {
                                                            viewModel.onPetTapped(pet)
                                                            onPetSelected(pet.id)
                                                        },
                                                        onToggleFavorite = { viewModel.toggleFavorite(pet) },
                                                        modifier = Modifier.width(160.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                items(state.pets, key = { it.id }) { pet ->
                                    PetCard(
                                        pet = pet,
                                        isFavorite = favoriteIds.contains(pet.id),
                                        onClick = {
                                            viewModel.onPetTapped(pet)
                                            onPetSelected(pet.id)
                                        },
                                        onToggleFavorite = { viewModel.toggleFavorite(pet) },
                                        modifier = Modifier.padding(6.dp)
                                    )
                                }
                                if (state.canLoadMore) {
                                    item(span = { GridItemSpan(2) }) {
                                        Box(
                                            modifier = Modifier.fillMaxSize().padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (state.isLoadingMore) {
                                                CircularProgressIndicator()
                                            } else {
                                                LaunchedEffect(state.pets.size) {
                                                    viewModel.loadNextPage()
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

    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            sheetState = sheetState
        ) {
            FilterSheetContent(
                initialFilter = filter,
                onApply = { newFilter ->
                    viewModel.updateFilter(newFilter)
                    scope.launch {
                        sheetState.hide()
                        showFilterSheet = false
                    }
                }
            )
        }
    }
}
