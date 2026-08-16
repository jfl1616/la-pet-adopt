package com.joseapps.lapetadopt.ui.browse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.joseapps.lapetadopt.data.model.PetAge
import com.joseapps.lapetadopt.data.model.PetGender
import com.joseapps.lapetadopt.data.model.PetSearchFilter
import com.joseapps.lapetadopt.data.model.PetSize
import com.joseapps.lapetadopt.data.model.PetType

@Composable
fun FilterSheetContent(
    initialFilter: PetSearchFilter,
    onApply: (PetSearchFilter) -> Unit
) {
    var location by remember { mutableStateOf(initialFilter.location) }
    var distance by remember { mutableStateOf(initialFilter.distanceMiles.toFloat()) }
    var type by remember { mutableStateOf(initialFilter.type) }
    var breedQuery by remember { mutableStateOf(initialFilter.breedQuery) }
    var ages by remember { mutableStateOf(initialFilter.ages) }
    var sizes by remember { mutableStateOf(initialFilter.sizes) }
    var genders by remember { mutableStateOf(initialFilter.genders) }
    var urgentOnly by remember { mutableStateOf(initialFilter.urgentOnly) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Filter pets", style = MaterialTheme.typography.titleLarge)

        FilterChip(
            selected = urgentOnly,
            onClick = { urgentOnly = !urgentOnly },
            label = { Text("🚨 Urgent only — needs a home now") }
        )

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location (city, state or ZIP)") },
            modifier = Modifier.fillMaxWidth()
        )

        Column {
            Text("Distance: ${distance.toInt()} miles", style = MaterialTheme.typography.labelLarge)
            Slider(value = distance, onValueChange = { distance = it }, valueRange = 5f..200f, steps = 38)
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Species", style = MaterialTheme.typography.labelLarge)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(vertical = 2.dp)) {
                items(PetType.entries.toTypedArray()) { option ->
                    FilterChip(
                        selected = type == option,
                        onClick = { type = if (type == option) null else option },
                        label = { Text("${option.emoji} ${option.label}") }
                    )
                }
            }
        }

        OutlinedTextField(
            value = breedQuery,
            onValueChange = { breedQuery = it },
            label = { Text("Name or breed (optional, e.g. \"Maple\" or \"Labrador\")") },
            modifier = Modifier.fillMaxWidth()
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Age", style = MaterialTheme.typography.labelLarge)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(vertical = 2.dp)) {
                items(PetAge.entries.toTypedArray()) { option ->
                    FilterChip(
                        selected = ages.contains(option),
                        onClick = { ages = if (ages.contains(option)) ages - option else ages + option },
                        label = { Text(option.label) }
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Size", style = MaterialTheme.typography.labelLarge)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(vertical = 2.dp)) {
                items(PetSize.entries.toTypedArray()) { option ->
                    FilterChip(
                        selected = sizes.contains(option),
                        onClick = { sizes = if (sizes.contains(option)) sizes - option else sizes + option },
                        label = { Text(option.label) }
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Gender", style = MaterialTheme.typography.labelLarge)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(vertical = 2.dp)) {
                items(PetGender.entries.toTypedArray()) { option ->
                    FilterChip(
                        selected = genders.contains(option),
                        onClick = { genders = if (genders.contains(option)) genders - option else genders + option },
                        label = { Text(option.label) }
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    onApply(
                        PetSearchFilter(
                            location = location,
                            distanceMiles = distance.toInt(),
                            type = type,
                            breedQuery = breedQuery,
                            ages = ages,
                            sizes = sizes,
                            genders = genders,
                            urgentOnly = urgentOnly
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Apply filters") }

            OutlinedButton(
                onClick = {
                    location = "Los Angeles, CA"
                    distance = 25f
                    type = null
                    breedQuery = ""
                    ages = emptySet()
                    sizes = emptySet()
                    genders = emptySet()
                    urgentOnly = false
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Reset") }
        }
    }
}
