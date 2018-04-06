package rolling.pandas.server.domain

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Embeddable

@Embeddable
data class Location(
        @JsonProperty("lat")
        var latitude: Double,
        @JsonProperty("lon")
        var longitude: Double
)