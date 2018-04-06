package rolling.pandas.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import rolling.pandas.server.domain.Place

interface PlaceRepository : JpaRepository<Place, Long> {
}