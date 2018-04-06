package rolling.pandas.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import rolling.pandas.server.domain.Event

interface EventRepository : JpaRepository<Event, Long> {

}