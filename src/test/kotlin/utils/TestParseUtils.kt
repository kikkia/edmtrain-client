package utils

import com.kikkia.edmtrain.utils.ParseUtils
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestParseUtils {

    // JSON for artist
    val artist1Json = JSONObject()
            .put("id", 1)
            .put("name", "test1")
    val artist2Json = JSONObject()
            .put("id", 2)
            .put("name", "test2")

    // JSON for Venues
    val venue1Json = JSONObject()
            .put("id", 1)
            .put("name", "venue1")
            .put("location", "a place")
            .put("address", "123 test place")
            .put("state", "AB")
            .put("latitude", 12.3)
            .put("longitude", 45.6)

    // JSON for Events
    val event1Json = JSONObject()
            .put("id", 1)
            .put("name", "event1")
            .put("link", "http://google.com/")
            .put("ticketLink", "http://google.com/ticket/")
            .put("ages", "18+")
            .put("festivalInd", true)
            .put("electronicGenreInd", true)
            .put("otherGenreInd", false)
            .put("date", "2020-06-09")
            .put("createdDate", "2019-05-23T17:42:20Z")
            .put("venue", venue1Json)
            .put("artistList", JSONArray()
                    .put(artist1Json)
                    .put(artist2Json))

    val event2Json = JSONObject()
            .put("id", 2)
            .put("name", "event2")
            .put("link", "http://bing.lul/")
            .put("ticketLink", "http://bing.lul/ticket/")
            .put("ages", "21+")
            .put("festivalInd", false)
            .put("electronicGenreInd", true)
            .put("otherGenreInd", false)
            .put("date", "2020-06-19")
            .put("createdDate", "2019-05-23T17:42:20Z")
            .put("venue", venue1Json)
            .put("artistList", JSONArray())

    @Test
    fun testParseJSONToEvents() {
        val json = JSONObject()
        json.put("success", true)

        json.put("data", JSONArray()
                .put(event1Json)
                .put(event2Json))

        val events = ParseUtils.parseEventsFromResponse(json)
        assertEquals(2, events.size)

        val event1 = events[0]
        assertEquals(event1.id, 1)
        assertEquals(event1.name, "event1")
        assertEquals(event1.link, "http://google.com/")
        assertEquals(event1.ticketLink, "http://google.com/ticket/")
        assertEquals(event1.ages, "18+")
        assertTrue(event1.isFestival)
        assertTrue(event1.isElectronic)
        assertFalse(event1.isOtherGenre)
        assertEquals("Tue Jun 09 00:00:00 CDT 2020", event1.date.toString())
        assertEquals("Thu May 23 17:42:20 CDT 2019", event1.created.toString())
        assertEquals(1, event1.venue.id)
        assertEquals("venue1", event1.venue.name)
        assertEquals("a place", event1.venue.location)
        assertEquals("123 test place", event1.venue.address)
        assertEquals("AB", event1.venue.state)
        assertEquals(12.3, event1.venue.latitude)
        assertEquals(45.6, event1.venue.longitude)
        assertEquals(1, event1.artists[0].id)
        assertEquals("test1", event1.artists[0].name)
        assertEquals(2, event1.artists[1].id)
        assertEquals("test2", event1.artists[1].name)

        val event2 = events[1]
        assertEquals(event2.id, 2)
        assertEquals(event2.name, "event2")
        assertEquals(event2.link, "http://bing.lul/")
        assertEquals(event2.ticketLink, "http://bing.lul/ticket/")
        assertEquals(event2.ages, "21+")
        assertFalse(event2.isFestival)
        assertTrue(event2.isElectronic)
        assertFalse(event2.isOtherGenre)
        assertEquals("Fri Jun 19 00:00:00 CDT 2020", event2.date.toString())
        assertEquals("Thu May 23 17:42:20 CDT 2019", event2.created.toString())
        assertEquals(1, event2.venue.id)
        assertEquals("venue1", event2.venue.name)
        assertEquals("a place", event2.venue.location)
        assertEquals("123 test place", event2.venue.address)
        assertEquals("AB", event2.venue.state)
        assertEquals(12.3, event2.venue.latitude)
        assertEquals(45.6, event2.venue.longitude)
        assertTrue(event2.artists.isEmpty())

    }
}