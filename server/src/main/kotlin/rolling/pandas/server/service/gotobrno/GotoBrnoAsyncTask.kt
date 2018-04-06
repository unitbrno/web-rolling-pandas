package rolling.pandas.server.service.gotobrno

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import rolling.pandas.server.dao.EventRepository
import rolling.pandas.server.domain.Event
import rolling.pandas.server.loggerFor
import javax.transaction.Transactional

@Service
class GotoBrnoAsyncTask(
        private val eventRepository: EventRepository,
        private val gotoBrnoParser: GotoBrnoParser
) {

    private val log = loggerFor(javaClass)

    @Scheduled(fixedDelay = 5000)
    @Transactional
    fun loadEventsFromGotoBrno() {
        log.info("starting")
        val eventsInDatabase: List<Event> = eventRepository.findAll()
        val events = gotoBrnoParser.getEvents()
        val eventsToPersist = events.filter { newEvent -> !eventsInDatabase.any { it.name == newEvent.name } }
        eventRepository.save(eventsToPersist)
        log.info("persisted ${eventsToPersist.size} new events")
    }
}
