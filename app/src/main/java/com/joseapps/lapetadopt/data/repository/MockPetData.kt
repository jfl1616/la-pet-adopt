package com.joseapps.lapetadopt.data.repository

import com.joseapps.lapetadopt.data.model.Pet
import com.joseapps.lapetadopt.data.model.PetAttributes
import com.joseapps.lapetadopt.data.model.PetEnvironment
import com.joseapps.lapetadopt.data.model.Shelter
import com.joseapps.lapetadopt.data.model.ShelterAddress
import com.joseapps.lapetadopt.data.model.ShelterCapacityStatus

/**
 * Sample data standing in for a real pet-listing API (Petfinder's API shut down December 2, 2025
 * — see the README for why, and for how to wire in a real replacement like RescueGroups.org).
 *
 * The six shelters below are the real LA Animal Services locations (names/addresses pulled from
 * laanimalservices.com/shelter-search); the pets themselves are made up so nothing here implies
 * a specific real animal is currently up for adoption.
 *
 * Photos: each pet has several real animal photos (not the same fictional pet, since no such
 * photos exist — these are stand-ins) pulled from free, attribution-free photo APIs so the app
 * shows genuine, breed-appropriate animals instead of generic stock/placeholder imagery:
 * dog photos from the Dog CEO API (dog.ceo, backed by the Stanford Dogs dataset), cat photos
 * from TheCatAPI (thecatapi.com), and rabbit photos from LoremFlickr (loremflickr.com, real
 * Flickr photos tagged "rabbit", pinned with a `lock` id so the same photos show every time).
 * Breed labels are occasionally an imperfect match to the stock photo (e.g. "American Bulldog"
 * shows a Boston/French Bulldog, since Dog CEO's dataset doesn't include that exact breed) —
 * acceptable here since every pet is already fictional sample data, not a real listing.
 */
object MockPetData {

    val shelters: List<Shelter> = listOf(
        Shelter(
            id = "laas-west-valley",
            name = "LA Animal Services — West Valley",
            email = "info@laanimalservices.com",
            phone = "(888) 452-7381",
            address = ShelterAddress("20655 Plummer St.", null, "Chatsworth", "CA", "91311", "US"),
            websiteUrl = "https://www.laanimalservices.com/shelter-search",
            missionStatement = "One of six City of Los Angeles animal shelters.",
            capacityStatus = ShelterCapacityStatus.NEAR_CAPACITY,
            capacityNote = "Running above target capacity for large-breed dogs."
        ),
        Shelter(
            id = "laas-west-la",
            name = "LA Animal Services — West Los Angeles",
            email = "info@laanimalservices.com",
            phone = "(888) 452-7381",
            address = ShelterAddress("11361 West Pico Blvd.", null, "Los Angeles", "CA", "90064", "US"),
            websiteUrl = "https://www.laanimalservices.com/shelter-search",
            missionStatement = "One of six City of Los Angeles animal shelters.",
            capacityStatus = ShelterCapacityStatus.NORMAL
        ),
        Shelter(
            id = "laas-chesterfield",
            name = "LA Animal Services — Chesterfield Square / South LA",
            email = "info@laanimalservices.com",
            phone = "(888) 452-7381",
            address = ShelterAddress("1850 W. 60th St.", null, "Los Angeles", "CA", "90047", "US"),
            websiteUrl = "https://www.laanimalservices.com/shelter-search",
            missionStatement = "One of six City of Los Angeles animal shelters.",
            capacityStatus = ShelterCapacityStatus.OVER_CAPACITY,
            capacityNote = "Over capacity — one of the busiest LA Animal Services locations."
        ),
        Shelter(
            id = "laas-north-central",
            name = "LA Animal Services — North Central",
            email = "info@laanimalservices.com",
            phone = "(888) 452-7381",
            address = ShelterAddress("3201 Lacy St.", null, "Los Angeles", "CA", "90031", "US"),
            websiteUrl = "https://www.laanimalservices.com/shelter-search",
            missionStatement = "One of six City of Los Angeles animal shelters.",
            capacityStatus = ShelterCapacityStatus.NEAR_CAPACITY,
            capacityNote = "Filling up, especially in the cattery."
        ),
        Shelter(
            id = "laas-harbor",
            name = "LA Animal Services — Harbor",
            email = "info@laanimalservices.com",
            phone = "(888) 452-7381",
            address = ShelterAddress("957 N. Gaffey St.", null, "San Pedro", "CA", "90731", "US"),
            websiteUrl = "https://www.laanimalservices.com/shelter-search",
            missionStatement = "One of six City of Los Angeles animal shelters.",
            capacityStatus = ShelterCapacityStatus.OVER_CAPACITY,
            capacityNote = "Over capacity, especially for senior and special-needs animals."
        ),
        Shelter(
            id = "laas-east-valley",
            name = "LA Animal Services — East Valley",
            email = "info@laanimalservices.com",
            phone = "(888) 452-7381",
            address = ShelterAddress("14409 Vanowen St.", null, "Van Nuys", "CA", "91405", "US"),
            websiteUrl = "https://www.laanimalservices.com/shelter-search",
            missionStatement = "One of six City of Los Angeles animal shelters.",
            capacityStatus = ShelterCapacityStatus.NORMAL
        )
    )

    private fun shelter(id: String) = shelters.first { it.id == id }

    // --- Real reference photos, grouped by source, so the pet() calls below stay readable. ---

    private fun dogPhotos(breed: String, vararg files: String) =
        files.map { "https://images.dog.ceo/breeds/$breed/$it" }

    private fun catPhotos(vararg ids: String) =
        ids.map { "https://s3.us-west-2.amazonaws.com/cdn2.thecatapi.com/images/$it.jpg" }

    private fun rabbitPhotos(vararg locks: Int) =
        locks.map { "https://loremflickr.com/700/700/rabbit,pet?lock=$it" }

    private val labradorPhotos = dogPhotos(
        "labrador", "n02099712_5338.jpg", "img_6236.jpg", "n02099712_3773.jpg", "louis4_%281%29.jpg"
    )
    private val chihuahuaPhotos = dogPhotos(
        "chihuahua", "n02085620_949.jpg", "n02085620_7292.jpg", "n02085620_2815.jpg", "n02085620_2614.jpg"
    )
    private val pitbullPhotos = dogPhotos(
        "pitbull", "siena-123.jpg", "pitbull_dog.jpg", "img_20190826_121528_876.jpg", "dog-3981540_1280.jpg"
    )
    private val borderColliePhotos = dogPhotos(
        "collie-border", "n02106166_1539.jpg", "n02106166_1452.jpg", "n02106166_5869.jpg", "n02106166_6437.jpg"
    )
    private val bulldogPhotos = listOf(
        "https://images.dog.ceo/breeds/bulldog-boston/n02096585_8396.jpg",
        "https://images.dog.ceo/breeds/bulldog-french/n02108915_2166.jpg",
        "https://images.dog.ceo/breeds/bulldog-boston/n02096585_7314.jpg",
        "https://images.dog.ceo/breeds/bulldog-boston/n02096585_1761.jpg"
    )
    private val germanShepherdPhotos = dogPhotos(
        "german-shepherd", "n02106662_14930.jpg", "n02106662_8870.jpg", "n02106662_22245.jpg", "n02106662_15398.jpg"
    )
    private val dachshundPhotos = dogPhotos(
        "dachshund", "dachshund-6.jpg", "sadie.jpg", "miniature_daschund.jpg", "dog-1018408_640.jpg"
    )
    private val goldenRetrieverPhotos = dogPhotos(
        "retriever-golden", "n02099601_2994.jpg", "n02099601_864.jpg", "n02099601_4678.jpg", "n02099601_5736.jpg"
    )
    private val rottweilerPhotos = dogPhotos(
        "rottweiler", "n02106550_2301.jpg", "n02106550_8595.jpg", "n02106550_5710.jpg", "n02106550_6599.jpg"
    )
    private val miniPoodlePhotos = dogPhotos(
        "poodle-miniature", "n02113712_3790.jpg", "n02113712_166.jpg", "n02113712_2552.jpg", "n02113712_3155.jpg"
    )
    private val beaglePhotos = dogPhotos(
        "beagle", "n02088364_12397.jpg", "n02088364_11509.jpg", "n02088364_16689.jpg", "n02088364_11458.jpg"
    )
    private val cattleDogPhotos = dogPhotos(
        "cattledog-australian", "img_4421.jpg", "img_4379.jpg", "img_5481.jpg", "img_7506.jpg"
    )
    private val newfoundlandPhotos = dogPhotos(
        "newfoundland", "n02111277_6213.jpg", "n02111277_1040.jpg", "n02111277_14601.jpg", "n02111277_5577.jpg"
    )
    private val huskyPhotos = dogPhotos(
        "husky", "n02110185_9396.jpg", "n02110185_5716.jpg", "n02110185_7210.jpg", "n02110185_6780.jpg"
    )

    private val domesticShorthair1Photos = catPhotos("a5u", "b2n", "d54", "c5k")
    private val domesticLonghairPhotos = catPhotos("8f6", "ams", "udZiLDG_E", "c6c")
    private val domesticShorthair2Photos = catPhotos("dEWWIiCgr", "z_k-oJ8xG", "b44", "5nm")
    private val domesticShorthair3Photos = catPhotos("3m4", "edl", "Sdsf0JSot", "brc")
    private val siamesePhotos = catPhotos("b7c", "ahm", "cjg", "bni")
    private val americanShorthairPhotos = catPhotos("oc", "MTY0MjEyNA", "c8c", "6dl")
    private val maineCoonPhotos = catPhotos("3ji", "a51", "vVF7hE-Py", "15l")
    private val britishShorthairPhotos = catPhotos("12j", "e3m", "uk0SrrBbQ", "b7t")

    private val shorthairRabbitPhotos = rabbitPhotos(1, 2, 3, 4)
    private val longhairRabbitPhotos = rabbitPhotos(11, 12, 13, 14)

    private fun pet(
        id: Long,
        name: String,
        type: String,
        primaryBreed: String,
        secondaryBreed: String? = null,
        isMixedBreed: Boolean = false,
        age: String,
        gender: String,
        size: String,
        description: String,
        shelterId: String,
        distanceMiles: Double,
        photoUrls: List<String>,
        spayedOrNeutered: Boolean = true,
        houseTrained: Boolean = false,
        specialNeeds: Boolean = false,
        shotsCurrent: Boolean = true,
        goodWithChildren: Boolean? = null,
        goodWithDogs: Boolean? = null,
        goodWithCats: Boolean? = null,
        isUrgent: Boolean = false
    ): Pet {
        val shelter = shelter(shelterId)
        val urgencyNote = if (isUrgent) {
            "\n\n🚨 Needs a home urgently — ${shelter.name} is ${shelter.capacityStatus.label.lowercase()}."
        } else {
            ""
        }
        return Pet(
            id = id,
            organizationId = shelterId,
            name = name,
            type = type,
            primaryBreed = primaryBreed,
            secondaryBreed = secondaryBreed,
            isMixedBreed = isMixedBreed,
            age = age,
            gender = gender,
            size = size,
            coat = null,
            description = "$description$urgencyNote\n\n(Sample listing for demo purposes — this is not a real, currently-adoptable animal. Photos are real animal reference photos, not photos of this specific fictional pet.)",
            photoUrls = photoUrls,
            status = "adoptable",
            distanceMiles = distanceMiles,
            petfinderUrl = "",
            attributes = PetAttributes(spayedOrNeutered, houseTrained, specialNeeds, shotsCurrent),
            environment = PetEnvironment(goodWithChildren, goodWithDogs, goodWithCats),
            contactEmail = shelter.email,
            contactPhone = shelter.phone,
            addressCity = shelter.address.city,
            addressState = shelter.address.state,
            isUrgent = isUrgent
        )
    }

    val pets: List<Pet> = listOf(
        pet(1, "Maple", "Dog", "Labrador Retriever", isMixedBreed = true, age = "Young", gender = "Female", size = "Large",
            description = "Maple is a goofy, tail-always-wagging girl who loves fetch and belly rubs.",
            shelterId = "laas-west-valley", distanceMiles = 3.2, photoUrls = labradorPhotos,
            houseTrained = true, goodWithChildren = true, goodWithDogs = true),
        pet(2, "Rocket", "Dog", "Chihuahua - Smooth Coated", age = "Adult", gender = "Male", size = "Small",
            description = "Rocket is a confident little guy who thinks he's much bigger than he is.",
            shelterId = "laas-west-la", distanceMiles = 5.8, photoUrls = chihuahuaPhotos, goodWithDogs = false),
        pet(3, "Biscuit", "Dog", "Pit Bull Terrier", isMixedBreed = true, age = "Adult", gender = "Male", size = "Large",
            description = "Biscuit is a big sweetheart looking for a patient family to help him decompress.",
            shelterId = "laas-chesterfield", distanceMiles = 4.1, photoUrls = pitbullPhotos,
            houseTrained = true, goodWithChildren = true, isUrgent = true),
        pet(4, "Willow", "Cat", "Domestic Shorthair", age = "Young", gender = "Female", size = "Small",
            description = "Willow is a curious tabby who loves sunny windowsills and string toys.",
            shelterId = "laas-north-central", distanceMiles = 2.5, photoUrls = domesticShorthair1Photos, goodWithCats = true),
        pet(5, "Gus", "Cat", "Domestic Longhair", age = "Senior", gender = "Male", size = "Medium",
            description = "Gus is a mellow old soul who just wants a warm lap and quiet company.",
            shelterId = "laas-harbor", distanceMiles = 8.9, photoUrls = domesticLonghairPhotos,
            goodWithCats = true, goodWithChildren = true, isUrgent = true),
        pet(6, "Nova", "Dog", "Border Collie", secondaryBreed = "Australian Shepherd", age = "Young", gender = "Female", size = "Medium",
            description = "Nova is whip-smart and high-energy — great for an active household.",
            shelterId = "laas-east-valley", distanceMiles = 6.3, photoUrls = borderColliePhotos,
            houseTrained = true, goodWithDogs = true),
        pet(7, "Tank", "Dog", "American Bulldog", isMixedBreed = true, age = "Adult", gender = "Male", size = "Large",
            description = "Tank is a big lovable lump who's happiest snoring on the couch.",
            shelterId = "laas-west-valley", distanceMiles = 3.6, photoUrls = bulldogPhotos,
            houseTrained = true, goodWithChildren = true, isUrgent = true),
        pet(8, "Clementine", "Cat", "Siamese", isMixedBreed = true, age = "Baby", gender = "Female", size = "Small",
            description = "Clementine is a playful kitten who pounces on absolutely everything.",
            shelterId = "laas-west-la", distanceMiles = 5.2, photoUrls = siamesePhotos, goodWithCats = true),
        pet(9, "Duke", "Dog", "German Shepherd Dog", age = "Adult", gender = "Male", size = "Large",
            description = "Duke is a loyal, watchful guy who bonds hard once he trusts you.",
            shelterId = "laas-chesterfield", distanceMiles = 4.4, photoUrls = germanShepherdPhotos,
            houseTrained = true, goodWithChildren = false, isUrgent = true),
        pet(10, "Pixel", "Rabbit", "Rabbit Shorthaired", age = "Young", gender = "Female", size = "Small",
            description = "Pixel is a sweet, quiet bunny who loves leafy greens and gentle pets.",
            shelterId = "laas-north-central", distanceMiles = 2.9, photoUrls = shorthairRabbitPhotos, spayedOrNeutered = true),
        pet(11, "Cocoa", "Dog", "Dachshund", isMixedBreed = true, age = "Senior", gender = "Female", size = "Small",
            description = "Cocoa is a sassy senior who still acts like a puppy at dinnertime.",
            shelterId = "laas-harbor", distanceMiles = 9.4, photoUrls = dachshundPhotos,
            houseTrained = true, goodWithDogs = true, isUrgent = true),
        pet(12, "Bandit", "Cat", "American Shorthair", isMixedBreed = true, age = "Adult", gender = "Male", size = "Medium",
            description = "Bandit is an independent guy who'll tolerate pets on his own schedule.",
            shelterId = "laas-east-valley", distanceMiles = 6.8, photoUrls = americanShorthairPhotos, goodWithCats = false),
        pet(13, "Sunny", "Dog", "Golden Retriever", isMixedBreed = true, age = "Young", gender = "Male", size = "Large",
            description = "Sunny is a walking wag machine who loves everyone he meets.",
            shelterId = "laas-west-valley", distanceMiles = 3.0, photoUrls = goldenRetrieverPhotos,
            houseTrained = true, goodWithChildren = true, goodWithDogs = true),
        pet(14, "Olive", "Cat", "Domestic Shorthair", age = "Adult", gender = "Female", size = "Medium",
            description = "Olive is a chatty tortie who narrates everything happening in the room.",
            shelterId = "laas-west-la", distanceMiles = 5.5, photoUrls = domesticShorthair2Photos, goodWithCats = true),
        pet(15, "Zeus", "Dog", "Rottweiler", isMixedBreed = true, age = "Adult", gender = "Male", size = "Extra Large",
            description = "Zeus is gentle giant material once he gets his zoomies out.",
            shelterId = "laas-chesterfield", distanceMiles = 4.7, photoUrls = rottweilerPhotos,
            houseTrained = true, goodWithChildren = false, isUrgent = true),
        pet(16, "Peanut", "Rabbit", "Rabbit Longhaired", age = "Adult", gender = "Male", size = "Small",
            description = "Peanut is a fluffy lop who binkies across the room when he's happy.",
            shelterId = "laas-north-central", distanceMiles = 3.1, photoUrls = longhairRabbitPhotos),
        pet(17, "Coco", "Dog", "Poodle - Miniature", isMixedBreed = true, age = "Baby", gender = "Female", size = "Small",
            description = "Coco is a curly-haired bundle of energy still learning her manners.",
            shelterId = "laas-harbor", distanceMiles = 9.0, photoUrls = miniPoodlePhotos,
            houseTrained = false, goodWithChildren = true),
        pet(18, "Shadow", "Cat", "Domestic Shorthair", isMixedBreed = true, age = "Young", gender = "Male", size = "Medium",
            description = "Shadow is sleek, athletic, and an expert at knocking things off shelves.",
            shelterId = "laas-east-valley", distanceMiles = 7.1, photoUrls = domesticShorthair3Photos, goodWithCats = true),
        pet(19, "Daisy", "Dog", "Beagle", isMixedBreed = true, age = "Adult", gender = "Female", size = "Medium",
            description = "Daisy has a nose for adventure and a bark to match her enthusiasm.",
            shelterId = "laas-west-valley", distanceMiles = 3.4, photoUrls = beaglePhotos,
            houseTrained = true, goodWithDogs = true),
        pet(20, "Milo", "Cat", "Maine Coon", isMixedBreed = true, age = "Senior", gender = "Male", size = "Large",
            description = "Milo is a gentle giant of a cat who loves being brushed.",
            shelterId = "laas-west-la", distanceMiles = 5.0, photoUrls = maineCoonPhotos,
            goodWithCats = true, goodWithChildren = true),
        pet(21, "Ruby", "Dog", "Australian Cattle Dog", age = "Adult", gender = "Female", size = "Medium",
            description = "Ruby is smart, driven, and needs a job to stay out of trouble.",
            shelterId = "laas-chesterfield", distanceMiles = 4.9, photoUrls = cattleDogPhotos,
            houseTrained = true, goodWithDogs = false, isUrgent = true),
        pet(22, "Simon", "Cat", "British Shorthair", isMixedBreed = true, age = "Adult", gender = "Male", size = "Medium",
            description = "Simon is a plush, dignified fellow who enjoys a good nap in a sunbeam.",
            shelterId = "laas-north-central", distanceMiles = 2.7, photoUrls = britishShorthairPhotos, goodWithCats = true),
        pet(23, "Bear", "Dog", "Newfoundland", isMixedBreed = true, age = "Adult", gender = "Male", size = "Extra Large",
            description = "Bear is a gentle 100-pound lap dog who thinks he's a lot smaller.",
            shelterId = "laas-harbor", distanceMiles = 8.6, photoUrls = newfoundlandPhotos,
            houseTrained = true, goodWithChildren = true, specialNeeds = true, isUrgent = true),
        pet(24, "Luna", "Dog", "Siberian Husky", isMixedBreed = true, age = "Young", gender = "Female", size = "Large",
            description = "Luna is an escape artist with a huge personality and huger howl.",
            shelterId = "laas-east-valley", distanceMiles = 6.5, photoUrls = huskyPhotos,
            houseTrained = true, goodWithDogs = true)
    )
}
