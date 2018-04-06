package rolling.pandas.server.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import rolling.pandas.server.dao.EventRepository
import rolling.pandas.server.domain.Event

@RestController
@RequestMapping("/event")
class EventController(private val eventRepository: EventRepository) {

    @GetMapping
    fun getAll(): List<Event> = eventRepository.findAll()
}