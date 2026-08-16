package com.joseapps.lapetadopt.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.joseapps.lapetadopt.data.model.Pet
import com.joseapps.lapetadopt.data.model.Shelter
import com.joseapps.lapetadopt.data.model.ShelterCapacityStatus
import com.joseapps.lapetadopt.ui.components.FullScreenError
import com.joseapps.lapetadopt.ui.components.FullScreenLoading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailScreen(
    viewModel: PetDetailViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val titleText = (uiState as? DetailUiState.Success)?.pet?.name?.let { "About $it" }
                        ?: "Pet details"
                    Text(titleText)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState is DetailUiState.Success) {
                        val isFavorite = (uiState as DetailUiState.Success).isFavorite
                        IconButton(onClick = viewModel::toggleFavorite) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        when (val state = uiState) {
            is DetailUiState.Loading -> FullScreenLoading(modifier = Modifier.padding(padding))
            is DetailUiState.Error -> FullScreenError(
                message = state.message,
                onRetry = viewModel::retry,
                modifier = Modifier.padding(padding)
            )
            is DetailUiState.Success -> PetDetailContent(
                pet = state.pet,
                shelter = state.shelter,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun PetDetailContent(pet: Pet, shelter: Shelter?, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if (pet.photoUrls.isNotEmpty()) {
            val pagerState = rememberPagerState(pageCount = { pet.photoUrls.size })
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth().aspectRatio(1f)) { page ->
                AsyncImage(
                    model = pet.photoUrls[page],
                    contentDescription = pet.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(pet.name, style = MaterialTheme.typography.titleLarge)
            Text(
                text = "${pet.breedLabel} · ${pet.age} · ${pet.gender} · ${pet.size}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (pet.distanceMiles != null) {
                Text(
                    text = "${"%.1f".format(pet.distanceMiles)} miles away",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (pet.isUrgent) {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Icon(Icons.Filled.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer)
                        Text(
                            text = "Needs a home urgently — see shelter status below.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            val traits = buildList {
                if (pet.attributes.spayedOrNeutered) add("Spayed/Neutered")
                if (pet.attributes.houseTrained) add("House-trained")
                if (pet.attributes.shotsCurrent) add("Shots current")
                if (pet.attributes.specialNeeds) add("Special needs")
                pet.environment.goodWithChildren?.let { if (it) add("Good with kids") }
                pet.environment.goodWithDogs?.let { if (it) add("Good with dogs") }
                pet.environment.goodWithCats?.let { if (it) add("Good with cats") }
            }
            if (traits.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(traits) { trait -> AssistChip(onClick = {}, label = { Text(trait) }) }
                }
            }

            Text("About ${pet.name}", style = MaterialTheme.typography.titleMedium)
            Text(pet.description, style = MaterialTheme.typography.bodyMedium)

            if (pet.petfinderUrl.isNotBlank()) {
                OutlinedButton(onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(pet.petfinderUrl)))
                }) {
                    Icon(Icons.Filled.OpenInNew, contentDescription = null)
                    Text("  View on Petfinder")
                }
            }

            ShelterSection(shelter = shelter, fallbackCity = pet.addressCity, fallbackState = pet.addressState)
        }
    }
}

@Composable
private fun ShelterSection(shelter: Shelter?, fallbackCity: String?, fallbackState: String?) {
    val context = LocalContext.current

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Filled.Pets, contentDescription = null)
                Text(shelter?.name ?: "Shelter", style = MaterialTheme.typography.titleMedium)
            }

            if (shelter != null && shelter.capacityStatus != ShelterCapacityStatus.NORMAL) {
                val capacityColor = if (shelter.capacityStatus == ShelterCapacityStatus.OVER_CAPACITY) {
                    MaterialTheme.colorScheme.error
                } else {
                    Color(0xFFB8860B) // amber — "near capacity" is a caution, not yet an alarm
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Filled.Warning, contentDescription = null, tint = capacityColor)
                    Column {
                        Text(
                            text = shelter.capacityStatus.label,
                            style = MaterialTheme.typography.labelLarge,
                            color = capacityColor
                        )
                        shelter.capacityNote?.let { note ->
                            Text(note, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            val addressLine = shelter?.displayAddress?.takeIf { it.isNotBlank() }
                ?: listOfNotNull(fallbackCity, fallbackState).joinToString(", ").ifBlank { null }

            if (addressLine != null) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Filled.Place, contentDescription = null, modifier = Modifier.padding(top = 2.dp))
                    Text(addressLine, style = MaterialTheme.typography.bodyMedium)
                }
            }

            shelter?.phone?.let { phone ->
                Text("Phone: $phone", style = MaterialTheme.typography.bodyMedium)
            }
            shelter?.missionStatement?.let { mission ->
                Text(mission, style = MaterialTheme.typography.bodyMedium)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (addressLine != null) {
                    OutlinedButton(onClick = {
                        val mapsUri = Uri.parse("geo:0,0?q=" + Uri.encode(addressLine))
                        val intent = Intent(Intent.ACTION_VIEW, mapsUri)
                        context.startActivity(intent)
                    }) { Text("Open in Maps") }
                }
                shelter?.websiteUrl?.let { website ->
                    OutlinedButton(onClick = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(website)))
                    }) { Text("Website") }
                }
            }
        }
    }
}
