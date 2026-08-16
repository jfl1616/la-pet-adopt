package com.joseapps.lapetadopt.data.model

/** Domain model for the shelter/rescue organization that lists a pet. */
data class Shelter(
    val id: String,
    val name: String,
    val email: String?,
    val phone: String?,
    val address: ShelterAddress,
    val websiteUrl: String?,
    val missionStatement: String?,
    val capacityStatus: ShelterCapacityStatus = ShelterCapacityStatus.NORMAL,
    val capacityNote: String? = null
) {
    /** Best-effort single-line address for display and for launching a Maps intent. */
    val displayAddress: String
        get() = listOfNotNull(
            address.address1,
            address.city,
            listOfNotNull(address.state, address.postcode).joinToString(" ").ifBlank { null }
        ).joinToString(", ")
}

data class ShelterAddress(
    val address1: String?,
    val address2: String?,
    val city: String?,
    val state: String?,
    val postcode: String?,
    val country: String?
)

/**
 * How full a shelter is running, illustrative sample data (see README) rather than a live
 * feed — municipal shelters don't publish capacity as an open API, just occasional gauges on
 * their own sites. Real data here would need a partnership with the shelter or manual updates.
 */
enum class ShelterCapacityStatus(val label: String) {
    NORMAL("Normal capacity"),
    NEAR_CAPACITY("Near capacity"),
    OVER_CAPACITY("Over capacity")
}
