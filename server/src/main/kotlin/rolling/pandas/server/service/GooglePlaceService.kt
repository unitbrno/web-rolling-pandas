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
import rolling.pandas.server.domain.Location
import rolling.pandas.server.domain.Place


class PlaceDeserializer : StdDeserializer<Place>(Place::class.java) {

    override fun deserialize(p0: JsonParser, p1: DeserializationContext): Place {
        val readTree = p0.codec.readTree<JsonNode>(p0)
        val googlePlaceDTO = Place(0)
        googlePlaceDTO.name = readTree.get("name").textValue()
        val location = readTree.get("geometry").get("location")
        googlePlaceDTO.location = Location(
                location.get("lat").doubleValue(),
                location.get("lng").doubleValue()
        )
        return googlePlaceDTO
    }
}

inline fun <reified T : Any> typeRef(): TypeReference<T> = object : TypeReference<T>() {}

@Service
@RestController("/brno")
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


    @GetMapping
    fun getPlacesInBrno(nextPageToken: String? = null): String {
        val restTemplate = RestTemplate()
        val response = restTemplate.getForEntity(fullUrl(nextPageToken), String::class.java)
        val objectMapper = ObjectMapper()
        val simpleModule = SimpleModule()
        simpleModule.addDeserializer(Place::class.java, PlaceDeserializer())
        objectMapper.registerModule(simpleModule)
        val jsonNode = objectMapper.readTree(response.body)
        val results = jsonNode.path("results")
        val placeReader = objectMapper.readerFor(typeRef<List<Place>>())
        val places = placeReader.readValue<Any>(results)

        val nextPageField = jsonNode.get("next_page_token")
        return if (nextPageField?.textValue() != null)
            places.toString() + getPlacesInBrno(nextPageField.textValue())
        else places.toString()
    }
}