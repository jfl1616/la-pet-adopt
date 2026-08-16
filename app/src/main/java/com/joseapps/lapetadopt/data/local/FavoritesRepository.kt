package com.joseapps.lapetadopt.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.joseapps.lapetadopt.data.model.Pet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Persists favorited pets to disk via Preferences DataStore, storing the full [Pet] snapshot
 * (not just an id) so the Favorites screen and pet detail screen both work fully offline —
 * no Room/SQLite needed for a list this small.
 */
class FavoritesRepository(private val dataStore: DataStore<Preferences>) {

    private val json = Json { ignoreUnknownKeys = true }
    private val favoritesKey = stringPreferencesKey("favorite_pets_json")

    val favorites: Flow<List<Pet>> = dataStore.data.map { prefs ->
        val raw = prefs[favoritesKey] ?: return@map emptyList()
        runCatching { json.decodeFromString<List<Pet>>(raw) }.getOrDefault(emptyList())
    }

    suspend fun isFavorite(petId: Long): Boolean =
        favorites.first().any { it.id == petId }

    suspend fun toggleFavorite(pet: Pet) {
        dataStore.edit { prefs ->
            val current = prefs[favoritesKey]?.let {
                runCatching { json.decodeFromString<List<Pet>>(it) }.getOrDefault(emptyList())
            } ?: emptyList()

            val updated = if (current.any { it.id == pet.id }) {
                current.filterNot { it.id == pet.id }
            } else {
                current + pet
            }
            prefs[favoritesKey] = json.encodeToString(updated)
        }
    }

    suspend fun remove(petId: Long) {
        dataStore.edit { prefs ->
            val current = prefs[favoritesKey]?.let {
                runCatching { json.decodeFromString<List<Pet>>(it) }.getOrDefault(emptyList())
            } ?: emptyList()
            prefs[favoritesKey] = json.encodeToString(current.filterNot { it.id == petId })
        }
    }
}
