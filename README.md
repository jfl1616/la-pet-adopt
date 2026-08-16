# LA Pet Adopt

An Android app (Kotlin + Jetpack Compose) for browsing adoptable dogs, cats, and other pets
from shelters around Los Angeles, saving favorites offline, and getting directions to the
shelter.

## ⚠️ About the data source: Petfinder shut down

This app was originally built against the [Petfinder API](https://www.petfinder.com/developers/),
which aggregated live listings from thousands of shelters and rescues nationwide. **Petfinder
shut its API down on December 2, 2025**, replacing it with a no-code website widget that isn't
usable from a mobile app. That wasn't knowable when this project started — the shutdown notice
only surfaced partway through building it.

**Right now the app runs on built-in sample data** (`MockPetRepository` /
`MockPetData.kt`) so it's fully functional and demoable without depending on any external
service. The sample pets are made up, but the six shelters they're attached to are the real LA
Animal Services locations (pulled live from laanimalservices.com/shelter-search), so the
shelter-info/"Open in Maps" flow is grounded in real places.

**Photos are real animal photos, not generic placeholders** — each pet has 4 photos (swipeable
on the detail screen) pulled from free photo APIs matched to its species/breed: dogs from the
[Dog CEO API](https://dog.ceo) (Stanford Dogs dataset), cats from [TheCatAPI](https://thecatapi.com),
and rabbits from [LoremFlickr](https://loremflickr.com) (real Flickr photos tagged "rabbit").
They're genuine photos of that kind of animal — just not photos of *this specific* fictional
pet, since no such photos exist. A couple of breed labels are an imperfect match to the stock
photo (e.g. "American Bulldog" shows a Boston/French Bulldog, since the Dog CEO dataset doesn't
include that exact breed) — fine here since every pet is already fictional. See
`MockPetData.kt` for the exact source of each photo set.

### If you want live data later

The closest legitimate replacement is **[RescueGroups.org's API](https://rescuegroups.org/services/adoptable-pet-data-api/)**
— it's free, aggregates listings from many shelters/rescues (coverage depends on which
organizations opted into their network, so it won't be a perfect 1:1 match for LA Animal
Services specifically), and its auth is a plain API-key header rather than Petfinder's OAuth2
dance. Getting a key means filling out [their request form](https://rescuegroups.org/services/request-an-api-key/)
and waiting for their team's approval — it's not instant self-serve like Petfinder was.

I looked into scraping LA Animal Services' own site (laanimalservices.com/search/pets) as a
more "authentically LA" alternative — it's a real, live listing of 1,000+ pets — but it's
server-rendered HTML from a Drupal site with no public JSON API (their photos are actually
served by petharbor.com under the hood, confirming they run on the old PetHarbor platform,
which likewise has no clean API — just HTML pages). Scraping either would be fragile, likely
against their terms, and a poor foundation for an app, so I didn't build on it.

**To wire in a real API once you have one:** write a new class implementing the `PetRepository`
interface (`data/repository/PetRepository.kt`) — same shape `MockPetRepository` already
implements — and point `AppContainer.petRepository` (`di/AppContainer.kt`) at it instead.
Nothing in the UI or ViewModel layer needs to change; that's the whole reason the repository
is behind an interface. Retrofit, OkHttp, and kotlinx.serialization are already in
`app/build.gradle.kts` and ready to use for that.

## Open and run in Android Studio

This project was written in a cloud sandbox that doesn't have the Android SDK or an emulator
installed, so it hasn't been run yet. To build and test it:

1. Open Android Studio, choose **Open**, and select this project's folder.
2. Let Gradle sync. **Dependency versions here were picked to be safely real and mutually
   compatible as of mid-2025** (AGP 8.7.2, Kotlin 2.0.21, Gradle 8.14.3, Compose BOM
   2024.09.00) rather than guessing at unverifiable bleeding-edge numbers. Android Studio may
   offer an **Upgrade Assistant** prompt or a "newer version available" suggestion for
   AGP/Kotlin/Compose — accepting those is safe and recommended. It may also ask you to pick a
   **Gradle JDK** (Settings → Build, Execution, Deployment → Build Tools → Gradle) — any JDK
   17–24 works; JDK 21 is a known-good pairing with this project's Gradle version.
3. Create/start an emulator via **Device Manager** (any recent phone profile, API 26+) if you
   don't already have one, or plug in a real Android device with USB debugging on.
4. Click **Run**. No API key or setup is needed to see the app working — it runs on sample
   data out of the box.

## What's implemented

- **Browse tab** — grid of adoptable pets with a filter sheet for species, breed (text
  search), age, size, gender, distance, and an "urgent only" toggle. A horizontally-scrolling
  "Needs a home urgently" row sits above the grid whenever there are flagged pets.
- **Pet detail screen** — photo carousel, key attributes (spayed/neutered, house-trained,
  shots current, good with kids/dogs/cats, etc.), full description, and the shelter's
  name/address/phone/mission with an **Open in Maps** button (launches the device's Maps app
  via an intent — no Google Maps API key needed). Urgent pets get a callout banner, and the
  shelter card shows a capacity indicator when a shelter is near/over capacity.
- **Favorites tab** — tap the heart on any pet to save it; favorites are stored on-device via
  Jetpack DataStore and are fully browsable offline, including their detail pages.

### Urgent/at-risk highlighting

To stand out from the other pet-adoption apps on the Play Store (Petfinder, Adopt-a-Pet, etc.),
pets can be flagged `isUrgent` and shelters carry a `capacityStatus` (normal / near capacity /
over capacity). Flagged pets get a red "URGENT" badge on their card, appear in the dedicated
banner row at the top of Browse, can be isolated with the filter-sheet toggle, and get a warning
callout plus their shelter's capacity status on the detail screen.

**This is illustrative sample data, not a live feed.** LA Animal Services doesn't publish a
machine-readable shelter-capacity API — the closest thing is a static gauge image
(`lacityvet.com/capacities/CapacityGuageDogs.png`) meant for humans to look at, not to parse.
The urgency/capacity values here are hand-set in `MockPetData.kt` to demonstrate what the
feature would look like; wiring it to something real would mean either finding a shelter data
source that publishes structured capacity/urgency data (RescueGroups.org's schema has fields
for this) or partnering directly with a shelter willing to share it.

## Notable scope decisions

- **Sample data instead of a live API** — see above.
- **Urgent/capacity flags are hand-set sample data, not a live feed** — see "Urgent/at-risk
  highlighting" above. The UI and data model (`Pet.isUrgent`, `Shelter.capacityStatus`) are
  built to hold real values the moment a real source for them exists.
- **DataStore instead of Room for favorites.** Room needs the KSP annotation-processor plugin,
  whose version has to be matched precisely to the Kotlin version — something this sandbox
  couldn't verify (no network access to Google's Maven repo here). Preferences DataStore storing
  serialized JSON needs no code generation at all, so it removes that whole risk while still
  giving fully offline favorites.
- **No Google Maps SDK / embedded map.** "Open in Maps" uses a plain `geo:` intent to the
  device's own Maps app, which avoids needing a second API key (Google Maps Platform, with its
  own billing setup) just to show a pin.

## Architecture

- **UI:** Jetpack Compose + Material 3, MVVM (`ViewModel` + `StateFlow`), Navigation Compose
  with a bottom bar (Browse / Favorites) and a pushed Detail screen.
- **Data:** a `PetRepository` interface with one implementation today (`MockPetRepository`,
  filtering/paginating in-memory sample data with a small artificial delay so loading states
  are visible). Retrofit + OkHttp + kotlinx.serialization are already set up as dependencies
  for whenever a real API replaces it.
- **Local storage:** Jetpack DataStore (Preferences) for favorites.
- **DI:** a small hand-rolled `AppContainer` (no Hilt/Dagger) — the dependency graph here is
  small enough that a DI framework's setup would add more risk than it removes.

## Next ideas if you want to keep building

- Get a RescueGroups.org API key and wire up a real `PetRepository` implementation.
- Swipe-card browsing mode as an alternative to the grid.
- Push notifications when a new pet matching your saved filters is posted.
- Share a pet's listing (Android share sheet) straight from the detail screen.
#   l a - p e t - a d o p t  
 