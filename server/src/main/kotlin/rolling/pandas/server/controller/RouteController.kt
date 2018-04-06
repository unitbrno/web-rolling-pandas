package rolling.pandas.server.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import rolling.pandas.server.domain.Location
import rolling.pandas.server.domain.Place
import rolling.pandas.server.domain.RoutePoint
import rolling.pandas.server.service.RouteCalculatorService


@RestController
@RequestMapping("/route")
class RouteController(private val routeCalculatorService: RouteCalculatorService) {

    @PostMapping
    fun calculateRoute(@RequestBody(required = false) places: List<RoutePoint>?): List<RoutePoint> =
            if (places == null) emptyList() else routeCalculatorService.calculateRoute(places)

}