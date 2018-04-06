package rolling.pandas.server.domain

import java.util.*

data class RoutePoint(
        val location: Location,
        val date: Date,
        val name: String
)