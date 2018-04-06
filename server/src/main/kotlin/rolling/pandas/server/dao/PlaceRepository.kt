package rolling.pandas.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import rolling.pandas.server.domain.Place

interface PlaceRepository : JpaRepository<Place, Long> {

    fun findByType(type: String): List<Place>
    fun findByName(name: String): List<Place>
    fun findByTypeAndName(type: String, name: String): List<Place>
}