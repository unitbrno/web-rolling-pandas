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

    override fun deserialize(p0: JsonParser?, p1: DeserializationContext?): Place {
        if (p0 != null && p1 != null) {
            val readTree = p0.codec.readTree<JsonNode>(p0)
            val googlePlaceDTO = Place(0)
            googlePlaceDTO.name = readTree.get("name").textValue()
            val location = readTree.get("geometry").get("location")
            googlePlaceDTO.location = Location(
                    location.get("lat").doubleValue(),
                    location.get("lng").doubleValue()
            )
            return googlePlaceDTO
        } else throw RuntimeException("grr, nulls")
    }
}

inline fun <reified T : Any> typeRef(): TypeReference<T> = object : TypeReference<T>() {}

@Service
@RestController("/brno")
class GoogleLocationService {
    private val KEY = "AIzaSyBWSRUkWSrChcNWNevU1pJktMb61aoA3Lg"
    private val LOCATION_OF_BRNO = Location(49.196227, 16.617108)
    private val RADIUS_IN_METER = 5000

    private val URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"

    fun fullUrl() = "$URL?key=$KEY&location=${LOCATION_OF_BRNO.latitude},${LOCATION_OF_BRNO.longitude}&radius=$RADIUS_IN_METER"

    @GetMapping
    fun getPlacesInBrno(): String {
        val objectMapper = ObjectMapper()
        val restTemplate = RestTemplate()
        val simpleModule = SimpleModule()
        simpleModule.addDeserializer(Place::class.java, PlaceDeserializer())
        objectMapper.registerModule(simpleModule)
        val response = restTemplate.getForEntity(fullUrl(), String::class.java)

        val jsonNode = objectMapper.readTree(response.body)
        val path = jsonNode.path("results")
        val readerFor = objectMapper.readerFor(typeRef<List<Place>>())
        val readValue = readerFor.readValue<Any>(path)
        return readValue.toString()
    }
}