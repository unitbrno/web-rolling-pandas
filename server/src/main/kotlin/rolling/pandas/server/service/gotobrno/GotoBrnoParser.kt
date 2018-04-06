package rolling.pandas.server.service.gotobrno

import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import rolling.pandas.server.domain.Event
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId


@Service
class GotoBrnoParser {
    private val url = "https://www.gotobrno.cz/sitemap.xml/"

    fun getAllLinksToEvents(): List<String> {
        val document = Jsoup.connect(url).get()
        return document.select("loc")
                .toList()
                .map { it.text() }
                .filter { it.startsWith("https://www.gotobrno.cz/en/actions/") }

    }

    fun getEvents() = getAllLinksToEvents().map {
        try {
            Thread.sleep(1000)
            return@map parseEvent(it)
        } catch (ex: Exception) {
            System.err.println("Error ${ex.message}")
            System.err.println("Could not parse $it")
            return@map Event(-1)
        }
    }.filter { it.id != -1L }

    fun parseEvent(url: String): Event {
        val titleClass = ".b-intro__title"
        val mainRowClass = ".row-main"
        val contentClass = ".b-content__annot"

        val document = Jsoup.connect(url).get()

        val event = Event(0)
        event.name = document.select(titleClass).text()
        event.description = document.select(contentClass).text()
        event.eventUrl = url
        try {
            val mainRow = document.select(mainRowClass).select("p.item-icon")
            val date = parseDate(mainRow[0].text().replace(".", ""))
            val (startTime, endTime) = parseDuration(mainRow[1].text())
            if (mainRow.size > 2)
                event.address = mainRow[2].text()

            LocalDate.from(date)
            event.startTime = date.atTime(LocalTime.parse(startTime))
            event.endTime = date.atTime(LocalTime.parse(endTime))
        } catch (ex: Exception) {
            System.err.println("Could not parse additional information for $url, reason: ${ex.message}")
        }
        return event
    }

    fun parseTime(input: String): Pair<Int, Int> {
        val split = input.split(":").map { it.toInt() }
        return split[0] to split[1]
    }

    fun parseDuration(input: String): Pair<String, String> {
        val split = input.split("–")
        return split[0] to split[1]
    }

    fun parseDate(input: String): LocalDate =
            SimpleDateFormat("dd MMM yyyy")
                    .parse(input)
                    .toInstant()
                    .atZone(ZoneId
                            .systemDefault())
                    .toLocalDate()

}

