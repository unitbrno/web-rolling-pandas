package rolling.pandas.server.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import rolling.pandas.server.dao.PlaceRepository
import rolling.pandas.server.domain.Place

@RestController
@RequestMapping("/place")
class PlaceController(private val placeRepository: PlaceRepository) {

    @GetMapping
    fun getAll(): List<Place> = placeRepository.findAll()
}