package com.joseapps.lapetadopt.data.repository

import com.joseapps.lapetadopt.data.model.Pet
import com.joseapps.lapetadopt.data.model.PetSearchFilter
import com.joseapps.lapetadopt.data.model.Shelter
import com.joseapps.lapetadopt.util.ApiResult
import kotlinx.coroutines.delay

/**
 * Stands in for a real network-backed [PetRepository] until this app is wired up to a live
 * pet-listing API (Petfinder's shut down Dec 2, 2025 — see README). Filters/paginates
 * [MockPetData] in memory so the Browse screen's filter sheet and "load more" still behave
 * like the real thing.
 */
class MockPetRepository(private val pageSize: Int = 8) : PetRepository {

    override suspend fun searchPets(filter: PetSearchFilter, page: Int): ApiResult<PetSearchPage> {
        delay(400) // a little latency so loading states are visible, like a real API call

        val matches = MockPetData.pets.filter { pet ->
            (filter.type == null || pet.type.equals(filter.type.label, ignoreCase = true)) &&
                (filter.breedQuery.isBlank() ||
                    pet.primaryBreed.contains(filter.breedQuery, ignoreCase = true) ||
                    pet.secondaryBreed?.contains(filter.breedQuery, ignoreCase = true) == true) &&
                (filter.ages.isEmpty() || filter.ages.any { it.label.equals(pet.age, ignoreCase = true) }) &&
                (filter.sizes.isEmpty() || filter.sizes.any { it.label.equals(pet.size, ignoreCase = true) }) &&
                (filter.genders.isEmpty() || filter.genders.any { it.label.equals(pet.gender, ignoreCase = true) }) &&
                (pet.distanceMiles == null || pet.distanceMiles <= filter.distanceMiles) &&
                (!filter.urgentOnly || pet.isUrgent)
        }

        val totalPages = ((matches.size - 1) / pageSize + 1).coerceAtLeast(1)
        val clampedPage = page.coerceIn(1, totalPages)
        val pageItems = matches.drop((clampedPage - 1) * pageSize).take(pageSize)

        return ApiResult.Success(
            PetSearchPage(pets = pageItems, currentPage = clampedPage, totalPages = totalPages)
        )
    }

    override suspend fun getPet(id: Long): ApiResult<Pet> {
        delay(200)
        val pet = MockPetData.pets.find { it.id == id }
            ?: return ApiResult.Error("That listing isn't available anymore.")
        return ApiResult.Success(pet)
    }

    override suspend fun getShelter(organizationId: String): ApiResult<Shelter> {
        delay(200)
        val shelter = MockPetData.shelters.find { it.id == organizationId }
            ?: return ApiResult.Error("Couldn't load shelter details.")
        return ApiResult.Success(shelter)
    }

    override suspend fun getUrgentPets(): ApiResult<List<Pet>> {
        delay(200)
        return ApiResult.Success(MockPetData.pets.filter { it.isUrgent })
    }
}
