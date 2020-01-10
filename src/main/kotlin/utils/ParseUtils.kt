package utils

import exceptions.APIException
import models.Artist
import models.Event
import models.Location
import models.Venue
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

object ParseUtils {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    private val createdDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")

    @JvmStatic
    fun parseEventsFromResponse(response: JSONObject) : List<Event> {
        if (!response.getBoolean("success")) {
            throw APIException(response.getString("message"))
        }
        val events = ArrayList<Event>()
        val data = response.getJSONArray("data")

        data.forEach {
            events.add(ParseUtils.mapJsonToEvent(it as JSONObject))
        }

        return events
    }


    private fun mapJsonToEvent(jsonObject: JSONObject) : Event {
        return Event(
                jsonObject.getInt("id"),
                jsonObject.getString("link"),
                jsonObject.getString("ticketLink"),
                jsonObject.getString("name"),
                jsonObject.getString("ages"),
                jsonObject.getBoolean("festivalInd"),
                jsonObject.getBoolean("electronicGenreInd"),
                jsonObject.getBoolean("otherGenreInd"),
                dateFormat.parse(jsonObject.getString("date")),
                createdDateFormat.parse(jsonObject.getString("createdDate")),
                mapJsonToVenue(jsonObject.getJSONObject("venue")),
                mapJSONArrayToArtistList(jsonObject.getJSONArray("artistList"))
        )
    }


    private fun mapJsonToVenue(json : JSONObject) : Venue {
        return Venue(
                json.getInt("id"),
                json.getString("name"),
                json.getString("location"),
                json.getString("address"),
                json.getString("state"),
                json.getDouble("latitude"),
                json.getDouble("longitude")
        )
    }


    private fun mapJSONArrayToArtistList(artistArr : JSONArray) : List<Artist> {
        val artists = ArrayList<Artist>()
        artistArr.forEach {
            artists.add(mapJsonToArtist(it as JSONObject))
        }
        return artists
    }


    private fun mapJsonToArtist(json : JSONObject) : Artist {
        return Artist(json.getInt("id"),
                json.getString("name"))
    }

    @JvmStatic
    fun parseLocationsFromResponse(response: JSONObject) : List<Location> {
        if (!response.getBoolean("success")) {
            throw APIException(response.getString("message"))
        }
        val locations = ArrayList<Location>()
        val data = response.getJSONArray("data")

        data.forEach {
            locations.add(ParseUtils.mapJsonToLocation(it as JSONObject))
        }

        return locations
    }

    private fun mapJsonToLocation(json : JSONObject) : Location {
        return Location(
                json.getInt("id"),
                json.getString("city"),
                json.getString("state"),
                json.getString("stateCode"),
                json.getDouble("latitude"),
                json.getDouble("longitude"),
                json.getString("link")
        )
    }
}
