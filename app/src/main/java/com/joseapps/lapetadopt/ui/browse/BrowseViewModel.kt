package com.joseapps.lapetadopt.ui.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseapps.lapetadopt.data.local.FavoritesRepository
import com.joseapps.lapetadopt.data.model.Pet
import com.joseapps.lapetadopt.data.model.PetSearchFilter
import com.joseapps.lapetadopt.data.repository.PetRepository
import com.joseapps.lapetadopt.di.SelectedPetHolder
import com.joseapps.lapetadopt.util.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface BrowseUiState {
    data object Loading : BrowseUiState
    data class Error(val message: String) : BrowseUiState
    data class Success(
        val pets: List<Pet>,
        val isLoadingMore: Boolean = false,
        val canLoadMore: Boolean = true
    ) : BrowseUiState
}

class BrowseViewModel(
    private val petRepository: PetRepository,
    private val favoritesRepository: FavoritesRepository,
    private val selectedPetHolder: SelectedPetHolder
) : ViewModel() {

    private val _filter = MutableStateFlow(PetSearchFilter())
    val filter: StateFlow<PetSearchFilter> = _filter

    private val _uiState = MutableStateFlow<BrowseUiState>(BrowseUiState.Loading)
    val uiState: StateFlow<BrowseUiState> = _uiState

    val favoriteIds: StateFlow<Set<Long>> = favoritesRepository.favorites
        .map { list -> list.map { it.id }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    private val _urgentPets = MutableStateFlow<List<Pet>>(emptyList())
    /** Backs the "needs a home urgently" banner row — loaded once, independent of the main filter/grid. */
    val urgentPets: StateFlow<List<Pet>> = _urgentPets

    private var currentPage = 1
    private var loadedPets = mutableListOf<Pet>()

    init {
        search()
        loadUrgentPets()
    }

    private fun loadUrgentPets() {
        viewModelScope.launch {
            when (val result = petRepository.getUrgentPets()) {
                is ApiResult.Success -> _urgentPets.value = result.data
                is ApiResult.Error -> Unit // Non-critical: banner just stays empty.
            }
        }
    }

    fun updateFilter(newFilter: PetSearchFilter) {
        _filter.value = newFilter
        search()
    }

    fun retry() = search()

    fun loadNextPage() {
        val state = _uiState.value
        if (state !is BrowseUiState.Success || state.isLoadingMore || !state.canLoadMore) return

        _uiState.value = state.copy(isLoadingMore = true)
        viewModelScope.launch {
            when (val result = petRepository.searchPets(_filter.value, currentPage + 1)) {
                is ApiResult.Success -> {
                    currentPage = result.data.currentPage
                    loadedPets.addAll(result.data.pets)
                    _uiState.value = BrowseUiState.Success(
                        pets = loadedPets.toList(),
                        canLoadMore = currentPage < result.data.totalPages
                    )
                }
                is ApiResult.Error -> {
                    // Keep showing what we already have; just stop the "loading more" spinner.
                    _uiState.value = state.copy(isLoadingMore = false)
                }
            }
        }
    }

    fun onPetTapped(pet: Pet) {
        selectedPetHolder.set(pet)
    }

    fun toggleFavorite(pet: Pet) {
        viewModelScope.launch { favoritesRepository.toggleFavorite(pet) }
    }

    private fun search() {
        currentPage = 1
        loadedPets = mutableListOf()
        _uiState.value = BrowseUiState.Loading
        viewModelScope.launch {
            when (val result = petRepository.searchPets(_filter.value, page = 1)) {
                is ApiResult.Success -> {
                    currentPage = result.data.currentPage
                    loadedPets = result.data.pets.toMutableList()
                    _uiState.value = BrowseUiState.Success(
                        pets = loadedPets.toList(),
                        canLoadMore = currentPage < result.data.totalPages
                    )
                }
                is ApiResult.Error -> _uiState.value = BrowseUiState.Error(result.message)
            }
        }
    }
}
