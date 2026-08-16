package com.joseapps.lapetadopt.data.model

import kotlinx.serialization.Serializable

/**
 * Domain model for an adoptable animal. Kept independent of any particular data source's wire
 * format (see [com.joseapps.lapetadopt.data.repository.PetRepository]) so swapping where pet
 * listings come from — currently sample data, see README — never touches the UI layer.
 */
@Serializable
data class Pet(
    val id: Long,
    val organizationId: String,
    val name: String,
    val type: String,
    val primaryBreed: String,
    val secondaryBreed: String?,
    val isMixedBreed: Boolean,
    val age: String,
    val gender: String,
    val size: String,
    val coat: String?,
    val description: String,
    val photoUrls: List<String>,
    val status: String,
    val distanceMiles: Double?,
    val petfinderUrl: String,
    val attributes: PetAttributes,
    val environment: PetEnvironment,
    val contactEmail: String?,
    val contactPhone: String?,
    val addressCity: String?,
    val addressState: String?,
    /** True if this listing is flagged as needing a home urgently (shelter over capacity,
     * long length of stay, etc). Default false keeps this field optional when decoding any
     * favorites that were saved before this field existed. */
    val isUrgent: Boolean = false
) {
    val breedLabel: String
        get() = when {
            secondaryBreed != null -> "$primaryBreed / $secondaryBreed"
            isMixedBreed -> "$primaryBreed Mix"
            else -> primaryBreed
        }

    val primaryPhotoUrl: String?
        get() = photoUrls.firstOrNull()
}

@Serializable
data class PetAttributes(
    val spayedOrNeutered: Boolean,
    val houseTrained: Boolean,
    val specialNeeds: Boolean,
    val shotsCurrent: Boolean
)

@Serializable
data class PetEnvironment(
    val goodWithChildren: Boolean?,
    val goodWithDogs: Boolean?,
    val goodWithCats: Boolean?
)
