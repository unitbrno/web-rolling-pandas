package rolling.pandas.server.service

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import rolling.pandas.server.domain.Location
import rolling.pandas.server.domain.Place
import rolling.pandas.server.domain.RoutePoint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@Service
@RestController()
class GoogleRouteService {

    private val KEY = "AIzaSyDXgSYHEZE-YC2dyUEZGaHBOZrim8ERKL0"
    private val URL = "https://maps.googleapis.com/maps/api/directions/json"

    @GetMapping("/routetest")
    fun getRoutesInBrno(): String {
        val dateformatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val objectMapper = ObjectMapper()
        val restTemplate = RestTemplate()
        //val simpleModule = SimpleModule()
        //simpleModule.addDeserializer(Place::class.java, PlaceDeserializer())
        //objectMapper.registerModule(simpleModule)
        val pa = RoutePoint("Place 1","","","STAY",
                "foobar", Location(49.2248262,16.5968045),
                dateformatter.parse("2018-04-07 09:00:00"),
                dateformatter.parse("2018-04-07 11:00:00"))

        val pb = RoutePoint("Place 2","","","STAY",
                "foobar", Location(49.1862765,16.6028591),
                dateformatter.parse("2018-04-07 12:00:00"),
                dateformatter.parse("2018-04-07 13:00:00"))
        val url = createRequestUrl(pa, pb)
        println(pb.startTime)
        println(url)
        val response = restTemplate.getForEntity(url, String::class.java)



        val jsonNode = objectMapper.readTree(response.body)
        val route = jsonNode.get("routes").toList()[0]

        var points = mutableListOf<RoutePoint>()

        for (leg in route.get("legs").toList()){
            for (step in leg.get("steps").toList()){

                val name = step.get("html_instructions").textValue()
                val description = ""
                val detail_url = ""
                val travel_mode = step.get("travel_mode").textValue()
                println(travel_mode)
                val action = if(travel_mode == "WALKING"){
                    "WALKING"
                } else {
                     step.get("transit_details").get("line").get("vehicle").get("type").textValue()
                }
                val start_int = if(travel_mode == "WALKING"){
                    if (points.isEmpty()) {
                        leg.get("departure_time").get("value").asLong()
                    } else {
                        points.last().endTime.time / 1000
                    }
                } else {
                    step.get("transit_details").get("departure_time").get("value").asLong()
                }
                val end_int = step.get("duration").get("value").asLong() + start_int

                val start  = Date(start_int * 1000)
                val end  = Date(end_int * 1000)

                val path = step.get("polyline").get("points").textValue()
                val location_obj = step.get("end_location")
                var location = Location(location_obj.get("lat").asDouble(),
                               location_obj.get("lng").asDouble())


                val point = RoutePoint(name, description, detail_url,
                                       action, path, location, start, end)
                points.add(point)
            }
            points.add(pb)
        }

        //val readerFor = objectMapper.readerFor(typeRef<List<Place>>())
        //val readValue = readerFor.readValue<Any>(path)
        return points.toString()
    }
    fun createRequestUrl(a : RoutePoint, b : RoutePoint) : String {

        val static = "$URL?key=$KEY&mode=transit"
        val origin = "origin=${a.location.latitude},${a.location.longitude}"
        val dest = "destination=${b.location.latitude},${b.location.longitude}"
        val arrival = "arrival_time=${b.startTime.time/1000}"
        return "$static&$origin&$dest&$arrival"
    }
}