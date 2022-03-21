package com.kikkia.edmtrain.utils

import com.kikkia.edmtrain.exceptions.APIException
import com.kikkia.edmtrain.models.Artist
import com.kikkia.edmtrain.models.Event
import com.kikkia.edmtrain.models.Location
import com.kikkia.edmtrain.models.Venue
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

object ParseUtils {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    private val createdDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

    @JvmStatic
    fun parseEventsFromResponse(response: JSONObject) : List<Event> {
        if (!response.getBoolean("success")) {
            throw APIException(response.getString("message"))
        }
        val events = ArrayList<Event>()
        val data = response.getJSONArray("data")

        data.forEach {
            events.add(mapJsonToEvent(it as JSONObject))
        }

        return events
    }


    private fun mapJsonToEvent(jsonObject: JSONObject) : Event {
        return Event(
                jsonObject.getInt("id"),
                jsonObject.getString("link"),
                jsonObject.optString("ticketLink", null),
                jsonObject.optString("name", null),
                jsonObject.optString("ages", null),
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
        if (json.getString("location") == "Virtual") {
            // Virtuals are an edge case
            return Venue(
                json.getInt("id"),
                json.getString("name"),
                json.getString("location"),
                "Online", "Online", 0.0, 0.0
            )
        }

        return Venue(
            json.getInt("id"),
            json.getString("name"),
            json.getString("location"),
            json.getString("address"),
            json.optString("state", "None"),
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
            locations.add(mapJsonToLocation(it as JSONObject))
        }

        return locations
    }

    private fun mapJsonToLocation(json : JSONObject) : Location {
        return Location(
                json.getInt("id"),
                json.optString("city", null),
                json.optString("state", null),
                json.optString("stateCode", null),
                json.getDouble("latitude"),
                json.getDouble("longitude"),
                json.optString("link", null)
        )
    }

    fun cleanArg(arg : String) : String {
        return arg.replace(" ", "%20")
    }
}
