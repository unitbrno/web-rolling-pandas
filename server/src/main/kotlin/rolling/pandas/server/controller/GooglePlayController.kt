package rolling.pandas.server.controller


import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import rolling.pandas.server.domain.Place
import rolling.pandas.server.service.google.place.GooglePlaceService

@RestController
class GooglePlayController(private val googlePlaceService: GooglePlaceService) {

    @GetMapping("/brno")
    fun get(): List<Place> = googlePlaceService.get()

    @GetMapping("/brno/{query}")
    fun getByQuery(@PathVariable query: String): List<Place> = googlePlaceService.getByQuery(query)

}