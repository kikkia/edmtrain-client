package com.kikkia.edmtrain

import com.kikkia.edmtrain.exceptions.APIException
import com.kikkia.edmtrain.models.Event
import com.kikkia.edmtrain.models.Location
import org.apache.commons.io.IOUtils
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.json.JSONObject
import com.kikkia.edmtrain.utils.ParseUtils

import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.HashMap

class EdmtrainClient(builder: Builder) {

    private val token: String?

    init {
        this.token = builder.token
    }

    fun queryForEvent() : EventQuery {
        return EventQuery()
    }

    fun queryForLocation() : LocationQuery {
        return  LocationQuery()
    }

    class Builder {
        internal var token: String? = null

        fun setToken(token: String) {
            this.token = token
        }

        fun build(): EdmtrainClient {
            return EdmtrainClient(this)
        }
    }

    /**
     * Allows the user to build and execute a query for events
     */
    inner class EventQuery {
        private val args: MutableMap<String, String>
        private val formatter: DateTimeFormatter

        init {
            args = HashMap()
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        }

        /**
         * Limit the query with a given event name
         */
        fun withEventName(name: String): EventQuery {
            this.args["eventName"] = name
            return this
        }

        /**
         * Limit the query with a given list of artist Ids
         */
        fun withArtistIds(artistIds: List<Int>): EventQuery {
            val idStrings = artistIds.map { it.toString() }
            this.args["artistIds"] = idStrings.joinToString(",")
            return this
        }

        /**
         * Limit the query with a given list of venue Ids
         */
        fun withVenueIds(venueIds: List<Int>): EventQuery {
            val idStrings = venueIds.map { it.toString() }
            this.args["venueIds"] = idStrings.joinToString(",")
            return this
        }

        /**
         * Limit the query with a list of location Ids
         */
        fun withLocationIds(locationIds: List<Int>): EventQuery {
            val idStrings = locationIds.map { it.toString() }
            this.args["locationIds"] = idStrings.joinToString(",")
            return this
        }

        /**
         * Limit the query to events occurring at or after this local date (only future events will be returned).
         */
        fun withStartDate(startDate: LocalDate): EventQuery {
            this.args["startDate"] = startDate.format(formatter)
            return this
        }

        /**
         * Limit the query to events occurring at or before this local date.
         */
        fun withEndDate(endDate: LocalDate): EventQuery {
            this.args["endDate"] = endDate.format(formatter)
            return this
        }

        /**
         * Limit the query to events added to Edmtrain at or after this UTC date (only future events will be returned).
         */
        fun withCreatedStartDate(createdStartDate: LocalDate): EventQuery {
            this.args["createdStartDate"] = createdStartDate.format(formatter)
            return this
        }

        /**
         * Limit the query to events added to Edmtrain at or before this UTC date (only future events will be returned).
         */
        fun withCreatedEndDate(createdEndDate: LocalDate): EventQuery {
            this.args["createdEndDate"] = createdEndDate.format(formatter)
            return this
        }

        /**
         * Limit the query to events that are festivals (true) or not festivals (false) (default: both)
         */
        fun isFestival(isFestival: Boolean): EventQuery {
            this.args["festivalInd"] = isFestival.toString()
            return this
        }


        /**
         * Set to true to include electronic shows (default is true). Set to false to exclude electronic shows.
         */
        fun includeElectronicGenre(includeElectronicGenre: Boolean): EventQuery {
            this.args["includeElectronicGenreInd"] = includeElectronicGenre.toString()
            return this
        }


        /**
         * Set to true to include non-electronic shows (default is false).
         */
        fun includeOtherGenre(includeOtherGenre: Boolean): EventQuery {
            this.args["includeOtherGenreInd"] = includeOtherGenre.toString()
            return this
        }

        /**
         * Executes the query
         */
        @Throws(APIException::class)
        fun get(): List<Event> {
            val url = buildUrl(this.args)
            try {
                HttpClients.createDefault().use { client ->
                    val get = HttpGet(url)
                    val response = client.execute(get)
                    val json = IOUtils.toString(response.entity.content)
                    val respJson = JSONObject(json)

                    try {
                        return ParseUtils.parseEventsFromResponse(respJson)
                    } catch (e : Exception) {
                        throw APIException("Something went wrong parsing the result: " + e.message)
                    }
                }
            } catch (e: IOException) {
                throw APIException("Something went wrong getting a connection to the api: " + e.message)
            }

        }
    }

    /**
     * Allows the consumer to build and execute a query for locations
     */
    inner class LocationQuery {
        private val args: MutableMap<String, String>

        init {
            args = HashMap()
        }

        /**
         *  (optional unless city is supplied) – The state or province of the desired location.
         */
        fun withState(state: String): LocationQuery {
            args["state"] = state
            return this
        }

        /**
         *  (optional) – The city or town of the desired location.
         */
        fun withCity(city: String): LocationQuery {
            args["city"] = city
            return this
        }

        /**
         * Executes the query
         */
        @Throws(APIException::class)
        fun get(): List<Location> {
            val url = buildUrl(this.args)

            try {
                HttpClients.createDefault().use { client ->
                    val get = HttpGet(url)
                    val response = client.execute(get)
                    val json = IOUtils.toString(response.entity.content)
                    val respJson = JSONObject(json)

                    return ParseUtils.parseLocationsFromResponse(respJson)
                }
            } catch (e: IOException) {
                throw APIException("Something went wrong getting a connection to the api: " + e.message)
            }

        }
    }

    private fun buildUrl(args: Map<String, String>): String {
        val baseUrl = "https://edmtrain.com/api/"
        val url = StringBuilder(baseUrl + "events?")
        for ((key, value) in args) {
            url.append(key).append("=").append(ParseUtils.cleanArg(value)).append("&")
        }
        return url.append("client=").append(token).toString()
    }
}
