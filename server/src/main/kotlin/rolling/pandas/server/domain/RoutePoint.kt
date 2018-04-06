package rolling.pandas.server.domain

import java.util.*

data class RoutePoint(
        val location: Location,
        val date: Date,
        val name: String


) {
    override fun toString(): String {
        return "RoutePoint(location=$location, date=$date, name='$name')"
    }
}