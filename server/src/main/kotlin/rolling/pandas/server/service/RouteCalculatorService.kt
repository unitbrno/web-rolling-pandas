package rolling.pandas.server.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import rolling.pandas.server.domain.Location
import rolling.pandas.server.domain.Place
import rolling.pandas.server.domain.RoutePoint
import java.text.SimpleDateFormat
import java.util.*

@Service
class RouteCalculatorService {
    private val KEY = "AIzaSyDXgSYHEZE-YC2dyUEZGaHBOZrim8ERKL0"
    private val URL = "https://maps.googleapis.com/maps/api/directions/json"
    //private val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    fun calculateRoute(places: List<RoutePoint>): List<RoutePoint> {
        val routePoints = mutableListOf<RoutePoint>()
        // don't get route for the first point
        for (place in places) {
            if (routePoints.isEmpty()){
                routePoints.add(place)
            } else { // find route between this and the previous route
                routePoints.addAll(getRoute(routePoints.last(), place))
            }
        }
        validateRoute(routePoints)
        return routePoints
    }

    fun getRoute(a : RoutePoint, b : RoutePoint) : List<RoutePoint> {

        val objectMapper = ObjectMapper()
        val restTemplate = RestTemplate()

        val url = createRequestUrl(a, b)
        val response = restTemplate.getForEntity(url, String::class.java)



        val jsonNode = objectMapper.readTree(response.body)
        val route = jsonNode.get("routes").toList()[0]

        val points = mutableListOf<RoutePoint>()

        for (leg in route.get("legs").toList()) {
            for (step in leg.get("steps").toList()) {


                val description = step.get("html_instructions").textValue()
                val detailUrl = ""
                val travelMode = step.get("travel_mode").textValue()

                val action = if (travelMode == "WALKING") {
                    "WALKING"
                } else {
                    step.get("transit_details").get("line").get("vehicle").get("type").textValue()
                }
                val name = if (travelMode == "WALKING") {
                    description
                } else {
                    step.get("transit_details").get("line").get("vehicle").get("name").textValue() +
                            " " + step.get("transit_details").get("line").get("short_name").textValue()
                }
                val startInt = if (travelMode == "WALKING") {
                    if (points.isEmpty()) {
                        leg.get("departure_time").get("value").asLong()
                    } else {
                        points.last().endTime.time / 1000
                    }
                } else {
                    step.get("transit_details").get("departure_time").get("value").asLong()
                }
                val endInt = step.get("duration").get("value").asLong() + startInt

                val start = Date(startInt * 1000)
                val end = Date(endInt * 1000)

                val path = step.get("polyline").get("points").textValue()
                val locationObj = step.get("end_location")
                val location = Location(locationObj.get("lat").asDouble(),
                        locationObj.get("lng").asDouble())


                val point = RoutePoint(name, description, detailUrl,
                        action, path, location, start, end)
                points.add(point)
            }
            points.add(b)
        }
        return points
    }

    fun createRequestUrl(a : RoutePoint, b : RoutePoint) : String {

        val static = "$URL?key=$KEY&mode=transit"
        val origin = "origin=${a.location.latitude},${a.location.longitude}"
        val dest = "destination=${b.location.latitude},${b.location.longitude}"
        val arrival = "arrival_time=${b.startTime.time/1000}"
        return "$static&$origin&$dest&$arrival"
    }

    fun validateRoute(route : List<RoutePoint>) {
        var minDate = route[0].startTime
        for (point in route){
            if (minDate > point.startTime || point.startTime > point.endTime){
                throw RuntimeException("scheduling is impossible")
            }
            minDate = point.endTime
        }
    }
}