package rolling.pandas.server.service.gotobrno

import org.springframework.stereotype.Service
import rolling.pandas.server.dao.EventRepository
import rolling.pandas.server.domain.Event
import rolling.pandas.server.loggerFor

@Service
class GotoBrnoAsyncTask(
        private val eventRepository: EventRepository,
        private val gotoBrnoParser: GotoBrnoParser
) {

    private val log = loggerFor(javaClass)

    //@Scheduled(initialDelay = 5 * 1000, fixedDelay = 1000 * 60 * 10)
    fun loadEventsFromGotoBrno() {
        log.info("starting")
        val eventsInDatabase: List<Event> = eventRepository.findAll()
        val events = gotoBrnoParser.getEvents()
        val eventsToPersist = events.filter { newEvent -> !eventsInDatabase.any { it.name == newEvent.name } }
        var errorCount = 0
        for (event in eventsToPersist) {
            try {
                eventRepository.save(event)
            } catch (e: Exception) {
                errorCount++
                log.error("Could not save event $event, reason: ${e.message}")
            }
        }
        log.info("persisted ${eventsToPersist.size - errorCount} new events, $errorCount errors")
    }
}
