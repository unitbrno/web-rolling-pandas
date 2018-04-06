package rolling.pandas.server.service.gotobrno

import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import rolling.pandas.server.domain.Event
import rolling.pandas.server.loggerFor
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId


@Service
class GotoBrnoParser {
    private val log = loggerFor(javaClass)
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
            val dateAsString = mainRow[0].text().replace(".", "")
            if ("The event has taken place" != dateAsString.trim()) {
                val date = parseDate(dateAsString)
                val (startTime, endTime) = parseDuration(mainRow[1].text())
                if (mainRow.size > 2)
                    event.address = mainRow[2].text()

                LocalDate.from(date)
                event.startTime = date.atTime(LocalTime.parse(startTime))
                event.endTime = if (endTime != null)
                    date.atTime(LocalTime.parse(endTime))
                else
                    date.atTime(LocalTime.parse(startTime))

            }
        } catch (ex: Exception) {
            log.info("Could not parse additional information for $url, reason: ${ex.message}")
            log.info("trying fallback parsing")
            val mainRow = document.select(mainRowClass).select("p.item-icon")
            val interval = parseTimeAsInterval(mainRow[0].text().replace(".", ""))
            if (interval != null) {
                event.startTime = interval.first.atTime(0, 0)
                event.startTime = interval.first.atTime(0, 0)
            } else {
                log.error("still did not success")
            }
        }
        return event
    }

    fun parseDuration(input: String): Pair<String, String?> {
        val split = input.split("–")
        return if (split.size == 2) split[0] to split[1] else split[0] to null
    }

    fun parseTimeAsInterval(input: String): Pair<LocalDate, LocalDate>? {
        try {
            val split = input.split("–").map { it.trim() }
            val startDate: LocalDate
            val endDate: LocalDate

            return if (isSpecifiedWithoutMonth(split[0])) {
                endDate = parseDate(split[1])
                startDate = LocalDate.of(endDate.year, endDate.month, split[0].toInt())
                startDate to endDate
            } else {
                startDate = parseDate(split[0] + " 2018")
                endDate = parseDate(split[1])
                LocalDate.of(endDate.year, startDate.month, startDate.dayOfMonth) to endDate
            }

        } catch (ex: Exception) {
            log.error("Could not parse $input, reason: ${ex.message}")
            return null
        }
    }

    private fun isSpecifiedWithoutMonth(input: String) =
            input.matches("\\d+".toRegex())

    fun parseDate(input: String): LocalDate =
            SimpleDateFormat("dd MMM yyyy")
                    .parse(input)
                    .toInstant()
                    .atZone(ZoneId
                            .systemDefault())
                    .toLocalDate()

}

