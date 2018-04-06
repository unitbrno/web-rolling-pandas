package rolling.pandas.server

import org.jsoup.Jsoup
import org.junit.Assert.assertEquals
import org.junit.Test
import rolling.pandas.server.service.gotobrno.GotoBrnoParser

class JsoupTest {

    @Test
    fun basicTest() {
        val url = "https://www.gotobrno.cz/sitemap.xml/"
        val connection = Jsoup.connect(url)
        val document = connection.get()
        for (element in document.select("loc")) {
            val text = element.text()
            println(text)
        }
    }

    @Test
    fun parseDateTest() {
        val gotoBrnoParser = GotoBrnoParser()
        val date = "13 june 2018"
        println(gotoBrnoParser.parseDate(date))
    }

    @Test
    fun parseTimeTest() {
        val gotoBrnoParser = GotoBrnoParser()
        val actual = gotoBrnoParser.parseDuration("19:00-20:00")
        assertEquals("19:00" to "20:00", actual)
    }

    @Test
    fun parseEventTest() {
        val gotoBrnoParser = GotoBrnoParser()
        val event = gotoBrnoParser.parseEvent("https://www.gotobrno.cz/en/actions/rythm-in-space-concert/")
        println(event)
    }
}