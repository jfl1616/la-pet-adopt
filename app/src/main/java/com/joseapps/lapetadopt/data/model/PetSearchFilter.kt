package com.joseapps.lapetadopt.data.model

/**
 * User-selected search filters for the browse screen. [location] defaults to Los Angeles
 * but stays editable so the app still works if Jose wants to point it at another city later.
 */
data class PetSearchFilter(
    val location: String = "Los Angeles, CA",
    val distanceMiles: Int = 25,
    val type: PetType? = null,
    val breedQuery: String = "",
    val ages: Set<PetAge> = emptySet(),
    val sizes: Set<PetSize> = emptySet(),
    val genders: Set<PetGender> = emptySet(),
    val urgentOnly: Boolean = false
)

enum class PetType(val apiValue: String, val label: String) {
    DOG("Dog", "Dog"),
    CAT("Cat", "Cat"),
    RABBIT("Rabbit", "Rabbit"),
    BIRD("Bird", "Bird"),
    SMALL_FURRY("Small & Furry", "Small & Furry"),
    HORSE("Horse", "Horse"),
    BARNYARD("Barnyard", "Barnyard"),
    SCALES_FINS_OTHER("Scales, Fins & Other", "Scales, Fins & Other")
}

enum class PetAge(val apiValue: String, val label: String) {
    BABY("baby", "Baby"),
    YOUNG("young", "Young"),
    ADULT("adult", "Adult"),
    SENIOR("senior", "Senior")
}

enum class PetSize(val apiValue: String, val label: String) {
    SMALL("small", "Small"),
    MEDIUM("medium", "Medium"),
    LARGE("large", "Large"),
    XLARGE("xlarge", "Extra Large")
}

enum class PetGender(val apiValue: String, val label: String) {
    MALE("male", "Male"),
    FEMALE("female", "Female")
}
