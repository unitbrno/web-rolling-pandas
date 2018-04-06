package rolling.pandas.server.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import rolling.pandas.server.domain.Event
import rolling.pandas.server.service.gotobrno.GotoBrnoParser

@RestController
class GotoBrnoController(private val gotoBrnoParser: GotoBrnoParser) {

    @GetMapping("/gotobrno")
    fun get(): List<Event> {
        return gotoBrnoParser.getEvents()
    }

}