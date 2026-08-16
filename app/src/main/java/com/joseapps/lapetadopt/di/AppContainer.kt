package com.joseapps.lapetadopt.di

import android.content.Context
import com.joseapps.lapetadopt.data.local.FavoritesRepository
import com.joseapps.lapetadopt.data.local.favoritesDataStore
import com.joseapps.lapetadopt.data.model.Pet
import com.joseapps.lapetadopt.data.repository.MockPetRepository
import com.joseapps.lapetadopt.data.repository.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Hand-rolled dependency graph (no Hilt/Dagger) — small enough for this app that a DI
 * framework and its annotation-processing setup would add more risk than value.
 *
 * [petRepository] currently points at [MockPetRepository] because Petfinder's API — the data
 * source this app was originally built against — shut down December 2, 2025. See the README
 * for the full story and for how to swap in a real API (RescueGroups.org is the closest
 * replacement) once you have credentials for one: write a new [PetRepository] implementation
 * and point this property at it. Nothing in the UI/ViewModel layer needs to change.
 */
class AppContainer(context: Context) {

    private val appContext = context.applicationContext

    val petRepository: PetRepository = MockPetRepository()

    val favoritesRepository = FavoritesRepository(appContext.favoritesDataStore)

    /** Carries the pet the user just tapped from Browse/Favorites into the Detail screen
     * without a network round-trip or nav-argument serialization gymnastics. */
    val selectedPetHolder = SelectedPetHolder()
}

class SelectedPetHolder {
    private val _pet = MutableStateFlow<Pet?>(null)
    val pet: StateFlow<Pet?> = _pet

    fun set(pet: Pet) {
        _pet.value = pet
    }
}
