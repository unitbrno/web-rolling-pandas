package rolling.pandas.server.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.MatrixVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import rolling.pandas.server.dao.PlaceRepository
import rolling.pandas.server.domain.Place

@RestController
@RequestMapping("/place")
class PlaceController(private val placeRepository: PlaceRepository) {

    @GetMapping
    fun getAll(@MatrixVariable(required = false) type: String?, @MatrixVariable(required = false) name: String?): List<Place> {
        return if (name != null && type != null)
            placeRepository.findByTypeAndName(type, name)
        else if (type != null)
            placeRepository.findByType(type)
        else if (name != null)
            placeRepository.findByName(name)
        else placeRepository.findAll()
    }
}