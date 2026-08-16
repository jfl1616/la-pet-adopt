package com.joseapps.lapetadopt.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseapps.lapetadopt.data.local.FavoritesRepository
import com.joseapps.lapetadopt.data.model.Pet
import com.joseapps.lapetadopt.di.SelectedPetHolder
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesRepository: FavoritesRepository,
    private val selectedPetHolder: SelectedPetHolder
) : ViewModel() {

    val favorites: StateFlow<List<Pet>> = favoritesRepository.favorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onPetTapped(pet: Pet) {
        selectedPetHolder.set(pet)
    }

    fun removeFavorite(petId: Long) {
        viewModelScope.launch { favoritesRepository.remove(petId) }
    }
}
