package rolling.pandas.server.service.facebook

import facebook4j.Event
import facebook4j.Facebook
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class FacebookService(private val facebook: Facebook) {

    @GetMapping("/facebook/{query}")
    fun get(@PathVariable query: String): List<Event> = facebook.searchEvents(query)
}