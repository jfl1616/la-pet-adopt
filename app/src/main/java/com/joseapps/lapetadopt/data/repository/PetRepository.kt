package com.joseapps.lapetadopt.data.repository

import com.joseapps.lapetadopt.data.model.Pet
import com.joseapps.lapetadopt.data.model.PetSearchFilter
import com.joseapps.lapetadopt.data.model.Shelter
import com.joseapps.lapetadopt.util.ApiResult

data class PetSearchPage(
    val pets: List<Pet>,
    val currentPage: Int,
    val totalPages: Int
)

/**
 * Abstraction over "wherever pet listings come from." [MockPetRepository] is the only
 * implementation right now (see its doc comment / the README for why) — swapping in a real
 * API later just means writing a new implementation of this interface and pointing
 * [com.joseapps.lapetadopt.di.AppContainer.petRepository] at it. Nothing in the UI layer
 * would need to change.
 */
interface PetRepository {
    suspend fun searchPets(filter: PetSearchFilter, page: Int): ApiResult<PetSearchPage>
    suspend fun getPet(id: Long): ApiResult<Pet>
    suspend fun getShelter(organizationId: String): ApiResult<Shelter>

    /** Pets flagged as needing a home urgently, for the Browse screen's top banner row. */
    suspend fun getUrgentPets(): ApiResult<List<Pet>>
}
