package com.joseapps.lapetadopt.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseapps.lapetadopt.data.local.FavoritesRepository
import com.joseapps.lapetadopt.data.model.Pet
import com.joseapps.lapetadopt.data.model.Shelter
import com.joseapps.lapetadopt.data.repository.PetRepository
import com.joseapps.lapetadopt.di.SelectedPetHolder
import com.joseapps.lapetadopt.util.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Error(val message: String) : DetailUiState
    data class Success(
        val pet: Pet,
        val isFavorite: Boolean,
        val shelter: Shelter?,
        val shelterLoading: Boolean
    ) : DetailUiState
}

class PetDetailViewModel(
    private val petId: Long,
    private val petRepository: PetRepository,
    private val favoritesRepository: FavoritesRepository,
    private val selectedPetHolder: SelectedPetHolder
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState

    init {
        load()
    }

    fun retry() = load()

    fun toggleFavorite() {
        val state = _uiState.value
        if (state !is DetailUiState.Success) return
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(state.pet)
            _uiState.value = state.copy(isFavorite = !state.isFavorite)
        }
    }

    private fun load() {
        _uiState.value = DetailUiState.Loading
        viewModelScope.launch {
            val pet = resolvePet()
            if (pet == null) {
                _uiState.value = DetailUiState.Error("Couldn't load this pet's details.")
                return@launch
            }

            val isFavorite = favoritesRepository.favorites.first().any { it.id == pet.id }
            _uiState.value = DetailUiState.Success(pet, isFavorite, shelter = null, shelterLoading = true)

            if (pet.organizationId.isNotBlank()) {
                when (val shelterResult = petRepository.getShelter(pet.organizationId)) {
                    is ApiResult.Success -> {
                        val current = _uiState.value
                        if (current is DetailUiState.Success) {
                            _uiState.value = current.copy(shelter = shelterResult.data, shelterLoading = false)
                        }
                    }
                    is ApiResult.Error -> {
                        val current = _uiState.value
                        if (current is DetailUiState.Success) {
                            _uiState.value = current.copy(shelterLoading = false)
                        }
                    }
                }
            } else {
                val current = _uiState.value
                if (current is DetailUiState.Success) {
                    _uiState.value = current.copy(shelterLoading = false)
                }
            }
        }
    }

    /** Prefer whatever's already in memory (just tapped, or a cached favorite) before hitting the network. */
    private suspend fun resolvePet(): Pet? {
        val fromHolder = selectedPetHolder.pet.value?.takeIf { it.id == petId }
        if (fromHolder != null) return fromHolder

        val fromFavorites = favoritesRepository.favorites.first().find { it.id == petId }
        if (fromFavorites != null) return fromFavorites

        return when (val result = petRepository.getPet(petId)) {
            is ApiResult.Success -> result.data
            is ApiResult.Error -> null
        }
    }
}
