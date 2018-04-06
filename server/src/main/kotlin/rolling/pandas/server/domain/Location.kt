package rolling.pandas.server.domain

import javax.persistence.Embeddable

@Embeddable
data class Location(var latitude: Double,
                    var longitude: Double)