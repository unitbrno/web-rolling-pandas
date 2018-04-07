package rolling.pandas.server.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class RoutePoint(
        val name: String,
        val description: String,
        @JsonProperty("detail_url")
        val detailUrl: String,
        val action: String,
        val path: String,
        val location: Location,
        @JsonProperty("start")
        val startTime: Date,
        @JsonProperty("end")
        val endTime: Date
) {

    override fun toString(): String {
        return "RoutePoint(name='$name', description='$description', detailUrl='$detailUrl', action='$action', path='$path', location=$location, startTime=$startTime, endTime=$endTime)"
    }
}