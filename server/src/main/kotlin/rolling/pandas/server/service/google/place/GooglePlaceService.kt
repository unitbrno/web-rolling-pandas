package rolling.pandas.server.service.google.place

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import rolling.pandas.server.domain.Location
import rolling.pandas.server.domain.Place


class PlaceDeserializer : StdDeserializer<Place>(Place::class.java) {

    override fun deserialize(jsonParser: JsonParser, context: DeserializationContext): Place {
        val root = jsonParser.codec.readTree<JsonNode>(jsonParser)
        val googlePlaceDTO = Place(0)
        googlePlaceDTO.name = root.get("name").textValue()
        val location = root.get("geometry").get("location")
        googlePlaceDTO.location = Location(
                location.get("lat").doubleValue(),
                location.get("lng").doubleValue()
        )
        googlePlaceDTO.iconUrl = root.get("icon").textValue()
        googlePlaceDTO.tags = root.get("types").toList().map { it.textValue() }
        return googlePlaceDTO
    }
}

inline fun <reified T : Any> typeRef(): TypeReference<T> = object : TypeReference<T>() {}

@Service
@RestController
class GooglePlaceService {
    private val KEY = "AIzaSyBWSRUkWSrChcNWNevU1pJktMb61aoA3Lg"
    private val LOCATION_OF_BRNO = Location(49.196227, 16.617108)
    private val RADIUS_IN_METER = 10000

    private val URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"

    fun fullUrl(nextPageToken: String?): String {
        var basicUrl = "$URL?key=$KEY&location=${LOCATION_OF_BRNO.latitude},${LOCATION_OF_BRNO.longitude}&radius=$RADIUS_IN_METER"
        if (nextPageToken != null) {
            basicUrl += "&pagetoken=$nextPageToken"
        }
        return basicUrl
    }


    @GetMapping("/brno")
    fun get(): List<Place> {
        val result = getPlacesInBrno()
        println("Loaded ${result.size} places ")
        return result
    }

    @GetMapping("/query/{query}")
    fun getByQuery(@PathVariable query: String): List<Place> {
        val url = "https://maps.googleapis.com/maps/api/place/textsearch/json?key=$KEY&query=$query"
        val restTemplate = RestTemplate()
        val response = restTemplate.getForEntity(url, String::class.java)
        var (places, next) = parseReponse(response)
        while (next != null) {
            val pair = parseReponse(restTemplate.getForEntity(url + "&pagetoken=$next", String::class.java))
            places += pair.first
            next = pair.second
        }
        return places
    }

    fun parseReponse(response: ResponseEntity<String>): Pair<List<Place>, String?> {
        val objectMapper = ObjectMapper()
        val simpleModule = SimpleModule()
        simpleModule.addDeserializer(Place::class.java, PlaceDeserializer())
        objectMapper.registerModule(simpleModule)
        val jsonNode = objectMapper.readTree(response.body)
        val results = jsonNode.path("results")
        val placeReader = objectMapper.readerFor(typeRef<List<Place>>())
        return placeReader.readValue<List<Place>>(results) to jsonNode.get("next_page_token")?.textValue()
    }

    fun getPlacesInBrno(nextPageToken: String? = null): List<Place> {
        val restTemplate = RestTemplate()
        val response = restTemplate.getForEntity(fullUrl(nextPageToken), String::class.java)
        val (places, next) = parseReponse(response)
        return if (next != null) {
            places + getPlacesInBrno(next)
        } else places
    }
}