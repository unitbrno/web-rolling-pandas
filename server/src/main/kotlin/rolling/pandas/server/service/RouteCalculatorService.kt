package rolling.pandas.server.service

import org.springframework.stereotype.Service
import rolling.pandas.server.domain.Location
import rolling.pandas.server.domain.RoutePoint
import java.util.*

@Service
class RouteCalculatorService {
    fun calculateRoute(points: List<Location>): List<RoutePoint> = points.map { RoutePoint("name", "desc", "url", "action", "path", Location(42.0, 42.0), Date(), Date()) }
}