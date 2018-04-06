package rolling.pandas.server.service.gotobrno

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import rolling.pandas.server.domain.Event

@RestController
class GotoBrnoController(private val gotoBrnoParser: GotoBrnoParser) {

    @GetMapping("/gotobrno")
    fun get(): List<Event> {
        return gotoBrnoParser.getEvents()
    }

}