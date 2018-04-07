package rolling.pandas.server.service.google.place

import org.springframework.stereotype.Service
import rolling.pandas.server.dao.PlaceRepository
import rolling.pandas.server.domain.Place
import rolling.pandas.server.domain.Topic
import rolling.pandas.server.loggerFor
import javax.transaction.Transactional

@Service
class GooglePlaceAsyncTask(private val googlePlaceService: GooglePlaceService, private val placeRepository: PlaceRepository) {

    private val log = loggerFor(javaClass)

    //@Scheduled(initialDelay = 5 * 1000, fixedDelay = 1000 * 60 * 10)
    @Transactional
    fun getAllPlaces() {
        log.info("started")
        val placesInDatabase: List<Place> = placeRepository.findAll()

        val loadedPlaces: List<Place> = Topic.values().map { it.name }
                .flatMap { topic ->
                    try {
                        return@flatMap googlePlaceService.getByQuery("$topic+brno")
                                .map {
                                    it.type = topic
                                    it
                                }
                    } catch (ex: Exception) {
                        log.error("Failed to load places in topic '$topic', reason: ${ex.message}")
                        return@flatMap emptyList<Place>()
                    }
                }
        val placesToPersist = loadedPlaces.filter { place -> !placesInDatabase.any { place.weakEquals(it) } }

        var errorCount = 0
        for (place in placesToPersist) {
            try {
                placeRepository.save(place)
            } catch (e: Exception) {
                errorCount++
                log.error("Could not save place $place, reason: ${e.message}")
            }
        }
        log.info("Finished, saved ${placesToPersist.size - errorCount} places, $errorCount errors")
    }
}